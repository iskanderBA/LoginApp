package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_forget_password extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    FirebaseAuth auth ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailEditText =(EditText) findViewById(R.id.email_reset);
        resetPasswordButton=(Button)findViewById(R.id.reset);
        auth= FirebaseAuth.getInstance();
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email =emailEditText.getText().toString().trim();
        if (email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("please provide valid email");
            emailEditText.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Activity_forget_password.this,"check your email to reset!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Activity_forget_password.this,"try again !!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}