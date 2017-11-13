package com.atrio.quesapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.quesapp.custom.CustomFabDialog;
import com.atrio.quesapp.model.QuessAnsModel;
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

public class QuestionAnswerActivity extends AppCompatActivity implements Animation.AnimationListener {

    String currentdeviceid, tittle, lang, qno_list;
    FirebaseUser user;
    DatabaseReference m_db;
    private FirebaseAuth mAuth;
    public TextView tv_tittle, tv_score, tv_quess, tv_ans,tv_showtext;
    int qno = 1;
    String qus_no;
            long total;
    Button bt_next,bt_back;
    FloatingActionButton fab;
    Animation animFadein, animMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_quess = (TextView) findViewById(R.id.tv_quesstion);
        tv_ans = (TextView) findViewById(R.id.tv_answer);
        tv_showtext = (TextView) findViewById(R.id.tv_showans);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_back= (Button) findViewById(R.id.bt_back);
        currentdeviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        user = FirebaseAuth.getInstance().getCurrentUser();
        m_db = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        tittle = i.getStringExtra("Sub");
        lang = i.getStringExtra("lang");
        qus_no =  i.getStringExtra("ques_no");
        //Log.i("qus_no22",""+qus_no);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown);
        animFadein.setAnimationListener(this);
        animMove.setAnimationListener(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentdeviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        user = mAuth.getCurrentUser();

       fab = (FloatingActionButton) findViewById(R.id.fab);


        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {

            tv_tittle.setText(tittle);
            if (qus_no != null){
                int qdata = Integer.parseInt(qus_no);
                qno_list = String.format("%03d", qdata);
                getQuestion(qno_list,qdata);
            }else{
                qno_list = String.format("%03d", qno);
                getQuestion(qno_list,qno);

            }




            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomFabDialog customFabDialog = new CustomFabDialog(QuestionAnswerActivity.this,tittle,lang,total);
                    customFabDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    customFabDialog.setCanceledOnTouchOutside(false);
                    customFabDialog.show();
                }
            });

            bt_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tv_ans.clearAnimation();

                    if (qus_no!=null){
//                        tv_ans.setVisibility(View.INVISIBLE);
                        int qdata = Integer.parseInt(qus_no);
                        qno=qdata;
                        qno++;
                        Log.i("qno44if",""+qno);
                        qno_list = String.format("%03d", qno);
                        getQuestion(qno_list, qno);
                        tv_quess.setText("");
                        tv_ans.setText("");
                        qus_no = null;

                    }else{
//                        tv_ans.setVisibility(View.INVISIBLE);
                        qno++;
                        Log.i("qno44else",""+qno);
                        qno_list = String.format("%03d", qno);
                        getQuestion(qno_list, qno);
                        tv_quess.setText("");
                        tv_ans.setText("");
//                        tv_ans.setVisibility(View.INVISIBLE);

                    }

                   /*     qno++;
                    Log.i("qno44",""+qno);*/


                }
            });

            bt_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tv_ans.clearAnimation();

                    if (qus_no!=null){
//                        tv_ans.setVisibility(View.INVISIBLE);
                        int qdata = Integer.parseInt(qus_no);
                        qno=qdata;
                        qno--;
                        Log.i("qno44if",""+qno);
                        qno_list = String.format("%03d", qno);
                        getQuestion(qno_list, qno);
                        tv_quess.setText("");
                        tv_ans.setText("");
                        qus_no = null;

                    }else{
//                        tv_ans.setVisibility(View.INVISIBLE);
                        qno--;
                        Log.i("qno44else",""+qno);
                        qno_list = String.format("%03d", qno);
                        getQuestion(qno_list, qno);
                        tv_quess.setText("");
                        tv_ans.setText("");
//                        tv_ans.setVisibility(View.INVISIBLE);

                    }

                }
            });

            tv_showtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_ans.setVisibility(View.VISIBLE);

                    tv_ans.startAnimation(animMove);

                }
            });

            try {
                checkuser();


            } catch (NullPointerException e) {

                Log.i("Exception33", e.getMessage());
            }

        }



       /* Log.i("qno_list11",""+qno_list);
        Log.i("tittle11",""+tittle);
        Log.i("lang11",""+lang);*/





  /* end*/


    }

    private void checkuser() throws NullPointerException {

        if (user == null) {

            throw new NullPointerException("user is null");
        } else {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query_realtimecheck = rootRef.child("UserDetail").orderByChild("emailId").equalTo(user.getEmail());
            Log.i("Querry66", "" + query_realtimecheck);
            query_realtimecheck.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                    String deviceid = userDetail.getDeviceId();
//                    Toast.makeText(QuestionAnswerActivity.this, "add" + deviceid, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(QuestionAnswerActivity.this, "addcurrent" + currentdeviceid, Toast.LENGTH_SHORT).show();
                    if (deviceid.equals(currentdeviceid)) {
//                        Toast.makeText(QuestionAnswerActivity.this, "add" + deviceid, Toast.LENGTH_SHORT).show();

                    } else {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(QuestionAnswerActivity.this, "You are logged in other device", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(QuestionAnswerActivity.this, "addelse" + deviceid, Toast.LENGTH_SHORT).show();
                        Toast.makeText(QuestionAnswerActivity.this, "You are logged in other device", Toast.LENGTH_SHORT).show();


                    }


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    //Toast.makeText(SubjectActivity.this,""+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(QuestionAnswerActivity.this, "change" + currentdeviceid, Toast.LENGTH_SHORT).show();
                    UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                    String deviceid = "data";
                    deviceid = userDetail.getDeviceId();
//                    Toast.makeText(QuestionAnswerActivity.this, "changecurrent" + deviceid, Toast.LENGTH_SHORT).show();
                    if (!deviceid.equals("data")) {

                        if (deviceid.equals(currentdeviceid)) {
//                            Toast.makeText(QuestionAnswerActivity.this, "chabgeif", Toast.LENGTH_SHORT).show();
                        } else {
                            mAuth.signOut();
//                            Toast.makeText(QuestionAnswerActivity.this, "changeelse", Toast.LENGTH_SHORT).show();
                            Toast.makeText(QuestionAnswerActivity.this, "You are logged in other device", Toast.LENGTH_SHORT).show();
                            Intent isend = new Intent(QuestionAnswerActivity.this, SplashActivity.class);
                            isend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(isend);
                            finish();


                        }
                    }


                    //Toast.makeText(SubjectActivity.this,"change"+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(QuestionAnswerActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

 /*
        Queery for  total question qnd attempted question
         */


    private void getQuestion(final String qno_list, final int qdata) {

//        tv_ans.setVisibility(View.INVISIBLE);

        Query querry_totalquess = m_db.child(lang).child("subjectList").child(tittle).orderByKey();
        // Log.i("datasnapshot79",""+querry_totalquess.getRef());

        querry_totalquess.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                total =dataSnapshot.getChildrenCount();
                String quess_no =  String.format("%03d", qdata);
                int finalNo = Integer.parseInt(quess_no);
                if (finalNo <= dataSnapshot.getChildrenCount()) {
                    tv_score.setText(qdata + "/" + total);
                }
               // Log.i("final44",""+finalNo);
                if (finalNo == dataSnapshot.getChildrenCount()){
                    bt_next.setEnabled(false);
                    bt_next.setBackgroundResource(R.color.centercolor);
                    tv_quess.setText(" No Question available right now");

                }else{
                    bt_next.setEnabled(true);
                    bt_next.setBackgroundResource(R.color.colorAccent);

                }

                if (finalNo == 1){
                    bt_back.setEnabled(false);
                    bt_back.setBackgroundResource(R.color.centercolor);
                }else{
                    bt_back.setEnabled(true);
                    bt_back.setBackgroundResource(R.color.colorAccent);
                }
                    if (dataSnapshot.getChildrenCount() != 0) {
                        tv_ans.setVisibility(View.INVISIBLE);

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                        // Log.i("datasnapshot77",""+data.getKey());
                        //Log.i("datasnapshot78",""+data.getValue());
                        String keydata = "Q-" + qno_list;
                        if (data.getKey().equals(keydata)) {
                            // Log.i("datasnapshot77",""+data.getKey());
//                             Log.i("datasnapshot78",""+data.getValue());
                            QuessAnsModel qModel = data.getValue(QuessAnsModel.class);

//                             Log.i("datasnapshot76",""+qModel.getAnswer());
//                             Log.i("datasnapshot75",""+qModel.getQuestion());
                            tv_quess.startAnimation(animFadein);
//                            tv_ans.startAnimation(animMove);
                            tv_quess.setText(qModel.getQuestion());
                            tv_ans.setText("Ans : " + qModel.getAnswer());
                        }else{


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

  /*

     End
         */


    }

    @Override
    public void onAnimationStart(Animation animation) {


    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
