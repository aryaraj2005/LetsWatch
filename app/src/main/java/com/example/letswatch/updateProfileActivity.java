package com.example.letswatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateProfileActivity extends AppCompatActivity {
   EditText uname;
   Button btnupdate;
   CircleImageView uimage;
   DatabaseReference dbreferance;
  StorageReference storageReference;
  Bitmap bitmap;
   Uri filepath;
   String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().hide();
        uname = findViewById(R.id.uname);
        uimage= findViewById(R.id.uimage);
        btnupdate = findViewById(R.id.updatebtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID  = user.getUid();
        dbreferance = FirebaseDatabase.getInstance().getReference().child("userprofile");
        storageReference = FirebaseStorage.getInstance().getReference();

         uimage.setOnLongClickListener(new View.OnLongClickListener(){
             @Override
             public boolean onLongClick(View v) {

                 Dexter.withContext(getApplicationContext())
                         .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                         .withListener(new PermissionListener() {
                             @Override
                             public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                 Intent intent = new Intent();
                                 intent.setType("image/*");
                                 intent.setAction(Intent.ACTION_GET_CONTENT);
                                 startActivityForResult(Intent.createChooser(intent , "Please select file") , 101);
                             }

                             @Override
                             public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                             }

                             @Override
                             public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                  permissionToken.continuePermissionRequest();
                             }
                         }).check();
                 return true;
             }
         });

         btnupdate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 upadtetofirebase();

             }
         });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if(requestCode == 101 && resultCode == RESULT_OK){
             filepath = data.getData();

             try {
                 // image is fetch when click over browser through uri
                 InputStream inputStream =getContentResolver().openInputStream(filepath);
                 bitmap = BitmapFactory.decodeStream(inputStream);
                 uimage.setImageBitmap(bitmap);
             }catch (Exception e){
                 Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
             }

         }
    }


    public void upadtetofirebase() {
        ProgressDialog pd  = new ProgressDialog(this);
        pd.setTitle("Profile update");
        pd.setMessage("uploading.....!!");
        pd.show();
        final  StorageReference uploder  = storageReference.child("profileimages/" + "img" + System.currentTimeMillis());
        uploder.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final HashMap<String, Object> map = new HashMap<>();
                                map.put("uimage" , uri.toString());
                                map.put("uname" , uname.getText().toString());

                                dbreferance.child(userID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            // hai to update kr do
                                            dbreferance.child(userID).updateChildren(map);
                                        }
                                        else {
                                            // nhi hai to set kar do
                                            dbreferance.child(userID).setValue(map);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                pd.dismiss();
                                Toast.makeText(updateProfileActivity.this, "updated Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float per = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        pd.setMessage("uploaded " + (int)per + " %");
                    }
                });


    }
    protected  void onStart() {

        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID= firebaseUser.getUid();
        dbreferance.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    uname.setText(snapshot.child("uname").getValue().toString());
                    Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}