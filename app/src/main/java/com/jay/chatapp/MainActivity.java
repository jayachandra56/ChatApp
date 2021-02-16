package com.jay.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    Button login,register;
    EditText email,password;
    String e_mail,e_password;
    ProgressBar progressBar;
    RelativeLayout progress_layout;

    FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);
        register=findViewById(R.id.signup);
        email=findViewById(R.id.email_id);
        password=findViewById(R.id.passord_id);
        progressBar=findViewById(R.id.progress);
        progress_layout=findViewById(R.id.prograss_layout);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(MainActivity.this,Dashboard.class);
            startActivity(intent);
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
               e_mail=email.getText().toString();
               e_password=password.getText().toString();
               mauth.signInWithEmailAndPassword(e_mail,e_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           progress_layout.setVisibility(View.GONE);
                           progressBar.setVisibility(View.GONE);
                           Intent intent=new Intent(MainActivity.this,Dashboard.class);
                           startActivity(intent);
                           finish();

                       }else {
                           progress_layout.setVisibility(View.GONE);
                           progressBar.setVisibility(View.GONE);
                           Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                       }
                   }
               });

            }
        });
    }
}