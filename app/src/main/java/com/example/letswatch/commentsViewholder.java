package com.example.letswatch;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentsViewholder extends RecyclerView.ViewHolder {
    CircleImageView imgs;
    TextView cusenm , cumsg , cudate , cutime;

    public commentsViewholder(@NonNull View itemView) {
        super(itemView);
        imgs = itemView.findViewById(R.id.cuprofileimg);
        cumsg = itemView.findViewById(R.id.textcomments);
        cudate = itemView.findViewById(R.id.date);
        cusenm = itemView.findViewById(R.id.username);
        cutime = itemView.findViewById(R.id.ctimes);
    }
}
