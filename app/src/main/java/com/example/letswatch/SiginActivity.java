package com.example.letswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SiginActivity extends AppCompatActivity {
    TextInputLayout t1 , t2;
    ProgressBar pb;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        t1 = findViewById(R.id.email_login);
        t2 = findViewById(R.id.emailpass_login);
        pb = findViewById(R.id.progressBar_logi);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity( new Intent(SiginActivity.this , Dashboard.class));
            finish();
        }
    }

    public void signinhere(View view) {
        pb.setVisibility(View.VISIBLE);
        String email = t1.getEditText().getText().toString();
        String password = t2.getEditText().getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SiginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pb.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(SiginActivity.this , Dashboard.class);
                            intent.putExtra("email" , mAuth.getCurrentUser().getEmail());
                            intent.putExtra(" uid" , mAuth.getCurrentUser().getUid());

                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            pb.setVisibility(View.INVISIBLE);
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(SiginActivity.this, "Invalid email/ password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}