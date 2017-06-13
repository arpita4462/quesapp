package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistraionActivity extends AppCompatActivity {

    private EditText mEmailView,mPasswordView;
    private ProgressBar mProgressView;
    private Button registraionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraion);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        registraionButton = (Button) findViewById(R.id.registraion_button);
        mProgressView = (ProgressBar) findViewById(R.id.registraion_progress);

        mAuth = FirebaseAuth.getInstance();

        registraionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
                mEmailView.setText("");
                mPasswordView.setText("");
            }
        });
    }

    private void registerUser(){

        //getting email and password from edit texts
        email = mEmailView.getText().toString().trim();
        password  = mPasswordView.getText().toString().trim();

        //checking if email and passwords are empty
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
        } else {
        //if the email and password are not empty
        //displaying a progress dialog

        mProgressView.setVisibility(View.VISIBLE);

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            Toast.makeText(RegistraionActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent =new Intent(RegistraionActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                            //display some message here
                            Toast.makeText(RegistraionActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        mProgressView.setVisibility(View.GONE);
                    }
                });

        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
}
