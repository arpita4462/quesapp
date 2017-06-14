package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
    private String email,password;
    TextInputLayout input_email,input_pwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraion);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        registraionButton = (Button) findViewById(R.id.registraion_button);
        mProgressView = (ProgressBar) findViewById(R.id.registraion_progress);
        input_email = (TextInputLayout)findViewById(R.id.input_email_id);
        input_pwd = (TextInputLayout)findViewById(R.id.input_password);

        mAuth = FirebaseAuth.getInstance();
        registraionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });
    }

    private void registerUser(){

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            input_email.setError(getString(R.string.err_msg_email));
        }else  if (TextUtils.isEmpty(password)) {
            input_email.setErrorEnabled(false);
            input_pwd.setError(getString(R.string.error_incorrect_password));
        }else {

        mProgressView.setVisibility(View.VISIBLE);

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistraionActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();

                            Intent intent =new Intent(RegistraionActivity.this,LoginActivity.class);
                            startActivity(intent);
                            mEmailView.setText("");
                            mPasswordView.setText("");
                        }else{
                            Toast.makeText(RegistraionActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        mProgressView.setVisibility(View.GONE);
                    }
                });

        }
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
