package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.quesapp.custom.CustomRestpwd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;

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
    private String email,password;
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


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                   // textView.setText(currentDateTimeString);
                    // User is signed in
                    startActivity(new Intent(LoginActivity.this,SubjectActivity.class));
                    finish();
//                    Log.i("signed_in:","" + user.getUid());
                } else {
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
        mAuth.addAuthStateListener(mAuthListener);
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

//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.i("failure111", "" + task.getException());
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),""+ task.getException()+"Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                    }

                }
            });
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }}

