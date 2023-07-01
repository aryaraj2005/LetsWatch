package com.example.letswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class CommentsPanels extends AppCompatActivity {

    EditText comnttext;
     ImageView sentimg;
     DatabaseReference userref , commentref;
     String poskey ;
     RecyclerView recyclerView;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_panels);
        recyclerView = findViewById(R.id.comntreceiver);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sentimg = findViewById(R.id.sentimg);
        comnttext = findViewById(R.id.edittextcomnt);
        // through that get get postkey of video from the dashboard
        poskey = getIntent().getStringExtra("poskey");
        // user reference from userprofile
        userref = FirebaseDatabase.getInstance().getReference().child("userprofile");
        // comments adding to the videos node by getting its reference
        commentref = FirebaseDatabase.getInstance().getReference().child("myvideo").child(poskey).child("comments");
        // user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // id
        final String userId = user.getUid();

        sentimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               userref.child(userId).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           // now taking the name and user image
                           String username = snapshot.child("uname").getValue().toString();
                           String  userimage = snapshot.child("uimage").getValue().toString();
                           proceescomments(username , userimage);
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }

            private void proceescomments(String username, String userimage) {
                String commentpost = comnttext.getText().toString();
                String randomkey = userId+ new Random().nextInt(1000) ;

                Calendar datevalu = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String commdate = dateFormat.format(datevalu.getTime());

                SimpleDateFormat timeformate = new SimpleDateFormat("HH:mm:ss");
                String commtime = timeformate.format(datevalu.getTime());

                HashMap map = new HashMap<>();
                map.put("usermsg" , commentpost);
               map.put("uid" , userId);
               map.put("username" , username);
               map.put("userimage" , userimage);
               map.put("date" , commdate);
               map.put("time" , commtime);

               // now fit the data
                commentref.child(randomkey).updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(CommentsPanels.this, "Comment sent successfully", Toast.LENGTH_SHORT).show();
                                      comnttext.setText("");
                                 }else{
                                     Toast.makeText(CommentsPanels.this, "not added ", Toast.LENGTH_SHORT).show();
                                 }
                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<commentModel> options =
                new FirebaseRecyclerOptions.Builder<commentModel>()
                        .setQuery(commentref, commentModel.class)
                        .build();
        FirebaseRecyclerAdapter<commentModel , commentsViewholder> recyclerAdapter = new FirebaseRecyclerAdapter<commentModel, commentsViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull commentsViewholder holder, int position, @NonNull commentModel model) {
                  holder.cusenm.setText(model.getUsername());
                  holder.cutime.setText( "Time" + model.getTime());
                  holder.cudate.setText("Date :" + model.getDate());
                  holder.cumsg.setText( model.getUsermsg());
                  Glide.with(holder.imgs.getContext()).load(model.getUserimage()).into(holder.imgs);
            }

            @NonNull
            @Override
            public commentsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooment_single_row , parent , false);
                return new commentsViewholder(view );
            }
        };
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);
    }




}