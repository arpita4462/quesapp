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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.quesapp.custom.CustomRestpwd;
import com.atrio.quesapp.custom.CustomUserVerification;
import com.atrio.quesapp.model.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView,mPasswordView;
    private TextView newUser,tv_forgetpwd;
    TextInputLayout input_email,input_pwd;
    private Button mEmailSignInButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String email,password,timeSettings,deviceid,currentdeviceid;
    private SpotsDialog dialog;
    private CustomRestpwd customRestpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        newUser=(TextView)findViewById(R.id.tv_newuser);
        tv_forgetpwd=(TextView)findViewById(R.id.tv_forgotpwd);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        input_email = (TextInputLayout)findViewById(R.id.input_email_id);
        input_pwd = (TextInputLayout)findViewById(R.id.input_password);

      dialog = new SpotsDialog(LoginActivity.this,R.style.Custom);

        currentdeviceid= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.i("currentdevice",""+currentdeviceid);
//        checkdeviceID();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

           user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    checkIfEmailVerified();
                    // User is signed in
                } else {
                    FirebaseAuth.getInstance().signOut();
                    // User is signed out
//                    Log.i("signed_out",""+user);
                }

            }
        };
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();


            }
        });


        newUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,RegistraionActivity.class);
                startActivity(intent);
            }
        });

        tv_forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRestpwd = new CustomRestpwd(LoginActivity.this);
                customRestpwd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customRestpwd.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            int autoTime = android.provider.Settings.System.getInt(getContentResolver(),android.provider.Settings.System.AUTO_TIME);
            if (autoTime!=1){
                Toast.makeText(getBaseContext(), "Turn on Automatic Date and Time", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                finish();

            }else
            {
                mAuth.addAuthStateListener(mAuthListener);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void attemptLogin() {
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            input_email.setError(getString(R.string.err_msg_email));
        }else  if (TextUtils.isEmpty(password)) {
            input_email.setErrorEnabled(false);
            input_pwd.setError(getString(R.string.error_incorrect_password));
        }else {
            dialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Log.i("success111", "" + task.isSuccessful());

                    } else {
                        Log.i("failure", "" + task.getException());
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                    }

                }
            });
        }
    }

    private void checkIfEmailVerified() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            try {


                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                Query userquery = rootRef.child("UserDetail").orderByChild("emailId").equalTo(user.getEmail());
                userquery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            UserDetail userDetail = dataSnapshot1.getValue(UserDetail.class);
                            deviceid = userDetail.getDeviceId();
                            if (!deviceid.equals(currentdeviceid)) {
                                FirebaseAuth.getInstance().signOut();
                                CustomUserVerification customUserVerification = new CustomUserVerification(LoginActivity.this);
                                customUserVerification.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                customUserVerification.show();


                            } else {
                                startActivity(new Intent(LoginActivity.this, SubjectActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            sendEmailVerify();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(LoginActivity.this, "Verify your Email.", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendEmailVerify() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

//                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }}

