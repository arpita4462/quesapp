package com.atrio.quesapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.atrio.quesapp.sendmail.SendMail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SendQuestionActivity extends AppCompatActivity {

    EditText et_ques,et_ans;
    Button btn_send;
    Spinner spinner;
    String sender_email,subject;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_question);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Log.i("userstatus",""+user);
        if (user==null){
            Toast.makeText(getBaseContext(), "You are logged out from this device", Toast.LENGTH_SHORT).show();
            Intent move = new Intent(SendQuestionActivity.this,LoginActivity.class);
            startActivity(move);
            finish();
        }
        et_ques=(EditText)findViewById(R.id.et_ques);
        et_ans=(EditText)findViewById(R.id.et_ans);
        btn_send=(Button) findViewById(R.id.btn_send);
        spinner= (Spinner) findViewById(R.id.spinner);

        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {

            mAuth=FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();

            Bundle b = getIntent().getExtras();

            if(b!=null){
                ArrayList<String> arr = (ArrayList<String>)b.getStringArrayList("array_list");
//                Log.i("array77156425",""+arr);
                ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arr);
                spinner.setAdapter(adapter);

            }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    subject = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(et_ques.getText()) && !TextUtils.isEmpty(et_ans.getText())){
                        sender_email=user.getEmail();
                        String email ="info@atrio.co.in";
                        String mail_subject = "Question";
                        String message = "Question-"+et_ques.getText()+"\n\nAnswer-"+et_ans.getText()+"\n\ncategory-"+subject +"\n\nSend By-"+sender_email;
                        SendMail sm = new SendMail(v.getContext(), email, mail_subject, message);
                        sm.execute();
                    }else {
                        Toast.makeText(getApplicationContext(), "Write Question and Answer", Toast.LENGTH_LONG).show();

                    }


                }
            });

        }








    }
}
