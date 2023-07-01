package com.example.letswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
    FloatingActionButton addvideo ;
   DatabaseReference reference;
   Intent intent;
   DatabaseReference userreference ;
   Boolean clickstate = false;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        reference = FirebaseDatabase.getInstance().getReference("likes");

        addvideo = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recview);
        addvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this , VideoAcitvity.class));

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        FirebaseRecyclerOptions<fileModel> options =
                new FirebaseRecyclerOptions.Builder<fileModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myvideo") , fileModel.class)
                        .build();

     FirebaseRecyclerAdapter<fileModel , myViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<fileModel, myViewholder>(options) {
         @Override
         protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull fileModel model) {

                holder.prepareexoplayer(getApplication() , model.getTitle() , model.getUrl() , model.getCuimgurl());


             // now taking the information of all the use
             // ofr the likes purpose
             // post  key refers to video id
             FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
             final String userID = users.getUid();
             final  String poskey = getRef(position).getKey();
             // for the image of evey user over the video  video id and userprofile url available in myvideo node just only yo fetch it
            //  Glide.with(holder.image.getContext()).load(FirebaseDatabase.getInstance().getReference().child("myvideo").child(poskey).child("cuimgurl\n")).into(holder.image);

              // just only check it status either liked or not
             holder.getlikestatus(poskey , userID);

             holder.commentsimag.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(Dashboard.this , CommentsPanels.class);
                     intent.putExtra("poskey" , poskey);
                     startActivity(intent);
                 }
             });
             // now go for liked
             holder.like.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     clickstate = true;
              reference.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(clickstate==true){
                            if(snapshot.child(poskey).hasChild(userID)){
                                // pahle se hi user available hai
                                  reference.child(poskey).child(userID).removeValue();
                                  clickstate = false;
                            }else {
                                    // user pahle seavailable nahi hai to insert kar do naye user ko
                                 reference.child(poskey).child(userID).setValue(true);
                                 clickstate  =false;
                            }
                        }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });
                 }
             });
         }

         @NonNull
         @Override
         public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design , parent , false);
             return  new myViewholder(view);
         }
     };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "log out ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Dashboard.this , MainActivity.class));
            finish();
        } else if (id == R.id.manageprofile) {
            startActivity(new Intent(Dashboard.this , updateProfileActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}