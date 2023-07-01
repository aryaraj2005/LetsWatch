package com.example.letswatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class VideoAcitvity extends AppCompatActivity {
    VideoView videoView;
    Button browsebtn  , uplobtn ;

    Uri videouri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference , userrefer;
    EditText vtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_acitvity);
         vtitle = findViewById(R.id.vtitle);
         storageReference = FirebaseStorage.getInstance().getReference();
         databaseReference = FirebaseDatabase.getInstance().getReference().child("/myvideo");
        videoView = findViewById(R.id.videoView);
        browsebtn = findViewById(R.id.browsebtn);
        uplobtn =   findViewById(R.id.uplodebtn);
        uplobtn.setVisibility(View.INVISIBLE);
        mediaController  = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

          browsebtn.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {
                  Dexter.withContext(getApplicationContext())
                          .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                          .withListener(new PermissionListener() {
                              @Override
                              public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                  Intent intent = new Intent();
                                  intent.setType("video/*");
                                  intent.setAction(Intent.ACTION_GET_CONTENT);
                                  startActivityForResult(intent , 101);
                              }

                              @Override
                              public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                              }

                              @Override
                              public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                  permissionToken.continuePermissionRequest();
                              }
                          }).check();
              }
          });
         uplobtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 processvideouploading();
             }

            
         });
    }
    public String getextension(){
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videouri));
    }




    public void processvideouploading() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.setMessage("under Uploading....!!!!");
        pd.show();
        userrefer = FirebaseDatabase.getInstance().getReference().child("userprofile");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       final  StorageReference uploader = storageReference.child("myvideo/"+System.currentTimeMillis()+"."+getextension());
           uploader.putFile(videouri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           // till here it is uploaded
                           // now taking the uri for RDB
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                         fileModel obj = new fileModel(vtitle.getText().toString() , uri.toString() , userrefer.child("uimage").toString() , user.getUid());
                                          databaseReference.child(databaseReference.push().getKey()).setValue(obj)
                                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void unused) {
                                                          pd.dismiss();
                                                          Toast.makeText(VideoAcitvity.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                                      }
                                                  }).addOnFailureListener(new OnFailureListener() {
                                                      @Override
                                                      public void onFailure(@NonNull Exception e) {
                                                          pd.dismiss();
                                                          Toast.makeText(VideoAcitvity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                                                      }
                                                  });
                                    }
                                });
                       }
                   })
                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                  float per =(snapshot.getBytesTransferred()*100/snapshot.getTotalByteCount());
                                  pd.setMessage((int)per + " % upload");
                       }
                   });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            browsebtn.setVisibility(View.INVISIBLE);
            uplobtn.setVisibility(View.VISIBLE);

           if (requestCode== 101 && resultCode == RESULT_OK)    {
               videouri = data.getData();
               videoView.setVideoURI(videouri);
           }

    }
}