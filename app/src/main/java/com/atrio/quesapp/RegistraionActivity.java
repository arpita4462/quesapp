package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atrio.quesapp.model.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistraionActivity extends AppCompatActivity {

    private EditText mEmailView,mPasswordView;
    private ProgressBar mProgressView;
    private Button registraionButton;
    private FirebaseAuth mAuth;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    SimpleDateFormat formatter;

    private String email,password,userName,createdDated,emailId,userId,deviceId;
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
        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("UserDetail");
        formatter = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
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
        }else  if (TextUtils.isEmpty(password) || password.length()<6) {
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

                            FirebaseUser user = mAuth.getCurrentUser();

                            Date dt = new Date();
                            userName = user.getEmail().substring(0,user.getEmail().indexOf("@"));
                            userId=user.getUid();
                            emailId=user.getEmail();
                            createdDated=formatter.format(dt);
                            deviceId= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                            Log.i("devicename",""+deviceId);
                            createUserDetail(userName,createdDated,emailId,userId,deviceId);
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(RegistraionActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(RegistraionActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            mEmailView.setText("");
                            mPasswordView.setText("");}
                        else{
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                            Toast.makeText(RegistraionActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegistraionActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        }
                        mProgressView.setVisibility(View.GONE);
                    }
                });

        }
    }

    private void createUserDetail(String userName, String createdDated, String emailId, String userId, String deviceId){

        UserDetail userDetail=new UserDetail();


        userDetail.setUserName(userName);
        userDetail.setCreatedDated(createdDated);
        userDetail.setEmailId(emailId);
        userDetail.setUserId(userId);
        userDetail.setDeviceId(deviceId);

        db_ref.child(userId).setValue(userDetail);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
