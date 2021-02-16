package com.jay.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register extends AppCompatActivity
{
    Button login,register;
    EditText email,password,username;
    String e_mail,e_password,e_username;

    FirebaseAuth mauth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // login=findViewById(R.id.login);
        register=findViewById(R.id.register_id_reg);
        email=findViewById(R.id.email_id_reg);
        password=findViewById(R.id.passord_id_reg);
        username=findViewById(R.id.username_id);
        mauth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                e_mail=email.getText().toString();
                e_password=password.getText().toString();
                e_username=username.getText().toString();
                mauth.createUserWithEmailAndPassword(e_mail,e_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=mauth.getCurrentUser();
                            String userid=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("users").child(userid);
                            HashMap<String,String> userDetails=new HashMap<>();
                            userDetails.put("id",userid);
                            userDetails.put("username",e_username);
                            userDetails.put("imgURL","default");

                            reference.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(register.this,MainActivity.class));
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"inside"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                        }

                        else{
                            Toast.makeText(register.this,"outside"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });
    }
}