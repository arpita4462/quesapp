package com.atrio.quesapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.atrio.quesapp.Adapter.CustomAdapter;
import com.atrio.quesapp.model.CustomSpinner;
import com.atrio.quesapp.sendmail.SendMail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class SendQuestionActivity extends AppCompatActivity {

    EditText et_ques,et_ans;
    Button btn_send;
    Spinner spinner;
    String sender_email;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<CustomSpinner> data;
    CustomSpinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_question);
        et_ques=(EditText)findViewById(R.id.et_ques);
        et_ans=(EditText)findViewById(R.id.et_ans);
        btn_send=(Button) findViewById(R.id.btn_send);
        spinner= (Spinner) findViewById(R.id.spinner);


        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            db_instance = FirebaseDatabase.getInstance();
            db_ref = db_instance.getReference();

            mAuth=FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            Query getsub=db_ref.orderByKey();
            data = new ArrayList<>();

            getsub.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() != 0){

                        Iterator<DataSnapshot> item = dataSnapshot.getChildren().iterator();

                        while (item.hasNext()) {

                            DataSnapshot items = item.next();
//                            String sub = items.getKey();
                            spin = new CustomSpinner();
                            spin.setSub(items.getKey());
                            Log.i("data771",""+items.getKey());
                            data.add(spin);

                        }
                    }else {

                        Toast.makeText(getApplicationContext(), "There is no Data", Toast.LENGTH_LONG).show();

                    }

/*
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String sub = child.getKey();
                        spin = new CustomSpinner();
                        if (!sub.equals("UserDetail")){
                            spin.setSub(child.getKey());
                            Log.i("data771",""+sub);
                            data.add(spin);
                        }


                    }
*/
                    Log.i("data772",""+data.size());
                    CustomAdapter customAdapter=new CustomAdapter(SendQuestionActivity.this,data);
                    spinner.setAdapter(customAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sender_email=user.getEmail();
                    String email ="info@atrio.co.in";
                    String mail_subject = "Question";
                    String message = "Question-"+et_ques.getText()+"\n\nAnswer-"+et_ans.getText()+"\n\nSend By-"+sender_email;
                    SendMail sm = new SendMail(v.getContext(), email, mail_subject, message);
                    sm.execute();
//                    Log.i("quesans",""+message);
                }
            });

        }








    }
}
