package com.example.letswatch;

import android.app.Application;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;

import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;


public class myViewholder extends RecyclerView.ViewHolder {

    TextView vtitletext  , liketext;
    PlayerView playerView;
    ImageView like , commentsimag , deleteimg;
    DatabaseReference reference , myvieorefernce;
     CircleImageView image;
      Intent intent;
      String poskey;
    public myViewholder(@NonNull View itemView) {
        super(itemView);
           playerView = itemView.findViewById(R.id.exoplayerview);
           vtitletext = itemView.findViewById(R.id.vtitle);
           liketext = itemView.findViewById(R.id.liketext);
           like = itemView.findViewById(R.id.likes);
           image = itemView.findViewById(R.id.circulaimg);
           deleteimg = itemView.findViewById(R.id.deletimg);
           commentsimag = itemView.findViewById(R.id.commetsimg);
    }

    public  void prepareexoplayer(Application application , String vtitle , String vurl ,  String imgurl) {

        // for the image of evey user over the video  video id and userprofile url available in myvideo node just only yo fetch it

                   vtitletext.setText(vtitle);

               ExoPlayer player = new ExoPlayer.Builder(application).build();
            // Bind the player to the view.
            playerView.setPlayer(player);
            // Build the media item.
            MediaItem mediaItem = MediaItem.fromUri(vurl);
// Set the media item to be played.
            player.setMediaItem(mediaItem);
// Prepare the player.
            player.prepare();
            player.setPlayWhenReady(false);
    }

    public void getlikestatus( final String poskey, final String userID) {
         reference = FirebaseDatabase.getInstance().getReference("likes")  ;
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 // now check either video available or not
                 if(snapshot.child(poskey).hasChild(userID)){
                     // no of likes will bw equal to to no. user available in likes video node
                       int no_oflikes = (int)snapshot.child(poskey).getChildrenCount();
                       liketext.setText(no_oflikes + " likes");
                       // if like then color get changes
                      like.setImageResource(R.drawable.baseline_favorite_24);
                 }else {
                     // if not liked
                     // no of likes will bw equal to to no. user available in likes video node
                     int no_oflikes = (int)snapshot.child(poskey).getChildrenCount();
                     liketext.setText(no_oflikes + "likes");
                     // if disike then color get changes
                     like.setImageResource(R.drawable.baseline_favorite_border_24);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }
}
