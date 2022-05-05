package com.example.loginapp.activities;

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

import com.example.loginapp.MainActivity;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Regsiter_user extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView banner, registerUser ;
    private EditText editTextFullName, editTextEmail, editTextAge,editTextPassword,editTextUsertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter_user);
        mAuth = FirebaseAuth.getInstance();
        banner=(TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);
        registerUser=(Button)findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName=(EditText) findViewById(R.id.fullname);
        editTextAge=(EditText) findViewById(R.id.age);
        editTextEmail=(EditText) findViewById(R.id.email);
        editTextPassword=(EditText) findViewById(R.id.password);
        editTextUsertype=(EditText) findViewById(R.id.userType);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.banner:
        startActivity(new Intent(this, MainActivity.class));
    case R.id.registerUser:
        registerUser();
        break;
        
        }

    }

    public void registerUser() {
        String email =editTextEmail.getText().toString().trim();
        String password =editTextPassword.getText().toString().trim();
        String fullName =editTextFullName.getText().toString().trim();
        String age =editTextAge.getText().toString().trim();
        String userType =editTextUsertype.getText().toString().trim();
editTextUsertype.setText("user");

        if (fullName.isEmpty()){
        editTextFullName.setError("full name is required !");
        editTextFullName.requestFocus();
        return;
    }
    if (age.isEmpty()){
        editTextAge.setError("age is required !");
        editTextAge.requestFocus();
        return;

    }
    if(email.isEmpty()){
        editTextEmail.setError("email is required");
        editTextEmail.requestFocus();
        return;
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        editTextEmail.setError("please provide valid email !");
        editTextEmail.requestFocus();
        return;
    }
if (password.isEmpty()){
    editTextPassword.setError("password is required !");
    editTextPassword.requestFocus();
    return;
}
if (password.length()<6){
    editTextPassword.setError("type at least 6 characters");
    editTextPassword.requestFocus();
    return;
}
//if (!editTextUsertype.getText().toString().equals("user") ){
 //editTextUsertype.setError("you must type user !! ");
  // editTextUsertype.requestFocus();
    //      return;
      //  }

mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  User user =new User(fullName,age,email,userType);
                  FirebaseDatabase.getInstance().getReference("Users")
                          .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                          .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull  Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Regsiter_user.this,"user has been registered successfully !",Toast.LENGTH_LONG).show();
                            //rediret to login layout

                        }else { Toast.makeText(Regsiter_user.this,"failed to register ! try again !!",Toast.LENGTH_LONG).show();
                        }}
                  });
              }  else{
                  Toast.makeText(Regsiter_user.this,"failed to register ! try again !!",Toast.LENGTH_LONG).show();

              }
            }
        });



    }
}