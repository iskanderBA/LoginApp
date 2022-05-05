package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.activities.DashboardAdminActivity;
import com.example.loginapp.activities.DashboardUserActivity;
import com.example.loginapp.activities.Regsiter_user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public TextView register, forgetPassword ;
    public EditText  editTextEmail, editTextPassword;
    public Button signin;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        register= findViewById(R.id.register);
        register.setOnClickListener(this);
        signin= findViewById(R.id.signIn);
        signin.setOnClickListener(this);
        editTextEmail= findViewById(R.id.email);
        editTextPassword= findViewById(R.id.password);
    mAuth= FirebaseAuth.getInstance();
    forgetPassword=(TextView) findViewById(R.id.forget_password);
    forgetPassword.setOnClickListener(this);




    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, Regsiter_user.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forget_password:
                startActivity(new Intent(this,Activity_forget_password.class));



        }
    }
    public void userLogin() {
        String email =editTextEmail.getText().toString().trim();
        String password =editTextPassword.getText().toString().trim();
        if (email.isEmpty()){
            editTextEmail.setError("email is empty !");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(" please enter a valid email !");
        }
        if(password.isEmpty()){
            editTextPassword.setError("password is required !");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassword.setError("type at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }
   //     progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                checkUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //login failed
                Toast.makeText(MainActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });






    }

    private void checkUser() {

        //get current user if logged in
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){

            //user logged in check user type,
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //get use type
                    String userType=""+snapshot.child("userType").getValue();
                    //check user type
                    if (userType.equals("user")){
                        startActivity(new Intent(MainActivity.this, DashboardUserActivity.class));
                        finish();
                    }
                    else if (userType.equals("admin")){
                        startActivity(new Intent(MainActivity.this, DashboardAdminActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}