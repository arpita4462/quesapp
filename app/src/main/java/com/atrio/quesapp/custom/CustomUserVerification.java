package com.atrio.quesapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atrio.quesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 19-07-2017.
 */

public class CustomUserVerification extends Dialog {

    Context mycontext;
    Button bt_send;
    EditText et_email;
    TextInputLayout inputLayoutName;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    public CustomUserVerification(Context context) {
        super(context);
        mycontext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        setContentView(R.layout.custom_emailverification);

        et_email = (EditText) findViewById(R.id.masked);
        bt_send = (Button) findViewById(R.id.btn_send_mail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputLayoutName = (TextInputLayout) findViewById(R.id.visible);

        auth = FirebaseAuth.getInstance();

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();

                if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
                    inputLayoutName.setError(getContext().getString(R.string.err_msg_email));
                    return;
                }
/*
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getContext(), "Failed to send reset email!. Try Again!!!!!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });*/

            }
        });


    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
