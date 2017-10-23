package com.atrio.quesapp;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.quesapp.model.QuessAnsModel;
import com.atrio.quesapp.model.QuestionModel;
import com.atrio.quesapp.model.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuestionAnswerActivity extends AppCompatActivity {

    String currentdeviceid, tittle, lang, qno_list;
    FirebaseUser user;
    DatabaseReference m_db;
    private FirebaseAuth mAuth;
    public TextView tv_tittle, tv_score, tv_quess, tv_ans;
    int qno = 001;
    Button bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        tv_tittle = (TextView) findViewById(R.id.tv_tittle);
       /* tv_score = (TextView) findViewById(R.id.tv_score);
        tv_quess = (TextView) findViewById(R.id.tv_quesstion);
        tv_ans = (TextView) findViewById(R.id.tv_answer);
        bt_next = (Button) findViewById(R.id.bt_next);
        currentdeviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        user = FirebaseAuth.getInstance().getCurrentUser();
        m_db = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        tittle = i.getStringExtra("Sub");
        lang = i.getStringExtra("lang");

        tv_tittle.setText(tittle);
        qno_list = String.format("%03d", qno);
        getQuestion(qno_list);

       *//* Log.i("qno_list11",""+qno_list);
        Log.i("tittle11",""+tittle);
        Log.i("lang11",""+lang);*//*

        *//**
         * Querry for login with another device with same email Id
         *//*
        Query query_realtimecheck = m_db.child("UserDetail").orderByChild("emailId").equalTo(user.getEmail());
        // Log.i("Querry66", "" + query_realtimecheck);
        query_realtimecheck.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                String deviceid = userDetail.getDeviceId();
               *//* Toast.makeText(QuestionAnswerActivity.this, "add" + deviceid, Toast.LENGTH_SHORT).show();
                Toast.makeText(QuestionAnswerActivity.this, "addcurrent" + currentdeviceid, Toast.LENGTH_SHORT).show();*//*
                if (deviceid.equals(currentdeviceid)) {
                    //Toast.makeText(QuestionAnswerActivity.this, "add" + deviceid, Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseAuth.getInstance().signOut();
                    //Toast.makeText(QuestionAnswerActivity.this, "addelse" + deviceid, Toast.LENGTH_SHORT).show();


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //Toast.makeText(SubjectActivity.this,""+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(QuestionAnswerActivity.this, "change" + currentdeviceid, Toast.LENGTH_SHORT).show();
                UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                String deviceid = userDetail.getDeviceId();
                // Toast.makeText(QuestionAnswerActivity.this, "changecurrent" + deviceid, Toast.LENGTH_SHORT).show();

                if (deviceid.equals(currentdeviceid)) {
                    //Toast.makeText(QuestionAnswerActivity.this, "chabgeif", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().signOut();
                    //Toast.makeText(QuestionAnswerActivity.this, "changeelse", Toast.LENGTH_SHORT).show();
                    Intent isend = new Intent(QuestionAnswerActivity.this, LoginActivity.class);
                    startActivity(isend);
                    finish();


                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(QuestionAnswerActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        *//*

        End
         *//*

         *//*
   Querry for  next question
   *//*

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qno++;
                qno_list = String.format("%03d", qno);
                getQuestion(qno_list);
            }
        });



  *//* end*//*


    }
 *//*
        Queery for  total question qnd attempted question
         *//*


    private void getQuestion(final String qno_list) {

        Query querry_totalquess = m_db.child(lang).child("subjectList").child(tittle).orderByKey();
        // Log.i("datasnapshot79",""+querry_totalquess.getRef());

        querry_totalquess.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String total = String.valueOf(dataSnapshot.getChildrenCount());
                String quess_no = String.valueOf(qno);
                if (qno <= dataSnapshot.getChildrenCount()) {
                    tv_score.setText(quess_no + "/" + total);
                }
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        // Log.i("datasnapshot77",""+data.getKey());
                        //Log.i("datasnapshot78",""+data.getValue());
                        String keydata = "Q-" + qno_list;
                        if (data.getKey().equals(keydata)) {
                            // Log.i("datasnapshot77",""+data.getKey());
                            // Log.i("datasnapshot78",""+data.getValue());
                            QuessAnsModel qModel = data.getValue(QuessAnsModel.class);

                            // Log.i("datasnapshot76",""+qModel.getAnswer());
                            // Log.i("datasnapshot75",""+qModel.getQuestion());
                            tv_quess.setText(qModel.getQuestion());
                            tv_ans.setText("Ans : " +    qModel.getAnswer());
                        }


                    }


                } else {
                    Toast.makeText(QuestionAnswerActivity.this, "There is no Questions", Toast.LENGTH_SHORT).show();


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

  *//*

     End
         *//*


   */ }
}