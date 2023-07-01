package com.example.letswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
      TextInputLayout t1 , t2;
      ProgressBar pb;
      Uri filepath;
      FirebaseAuth mAuth;
      CircleImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        t1 = findViewById(R.id.email);
        t2 = findViewById(R.id.emailpass);
        pb = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity( new Intent(MainActivity.this , Dashboard.class));
            finish();
        }

    }


           // direct created a methood from xml layout so that no required to initialised and use set in click listener
           public void gotosigin(View view) {
              startActivity(new Intent(MainActivity.this, SiginActivity.class));
              }
    public void signuphere(View view) {
        pb.setVisibility(View.VISIBLE);
        String email = t1.getEditText().getText().toString();
        String password = t2.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pb.setVisibility(View.INVISIBLE);
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this , Dashboard.class));

                        } else {
                            pb.setVisibility(View.INVISIBLE);
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(MainActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}