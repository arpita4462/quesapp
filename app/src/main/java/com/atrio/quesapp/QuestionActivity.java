package com.atrio.quesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atrio.quesapp.StoreData.SharedPreference;
import com.atrio.quesapp.model.ListData;
import com.atrio.quesapp.model.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
/*import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;*/

import java.lang.reflect.Type;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class QuestionActivity extends AppCompatActivity implements Animation.AnimationListener,View.OnClickListener {

    RadioGroup rg_option;
    RadioButton rb_opA,rb_opB,rb_opC,rb_opD,rbselect,rbcorrect;
    Button btn_sub,bt_done,bt_pos1,bt_pos2,bt_pos3,bt_pos4,bt_pos5,bt_pos6,bt_pos7,bt_pos8,bt_pos9,bt_pos10;
    TextView tv_sub,tv_ques,tv_quesno;
    ImageView img_ques;
    Animation animFadein,animMove;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    public String tittle,correctAns,selectedAns;
    int qno=001,correctValue =0,checkedRadioButtonID,total_question=0;
    SpotsDialog dialog;
   ArrayList<String> arrayList;
    ArrayList<ListData>  list_data,all_quesData;
    public String ques_noList;
    int value,checkedSelectId;
    SharedPreference sharedData;
    SharedPreferences sharedPrefs;
    ListData listData1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        tv_sub=(TextView)findViewById(R.id.tv_sub);
        tv_ques=(TextView)findViewById(R.id.tv_ques);
        tv_quesno =(TextView)findViewById(R.id.tv_no);
        rb_opA=(RadioButton) findViewById(R.id.rb_opA);
        rb_opB=(RadioButton) findViewById(R.id.rb_opB);
        rb_opC=(RadioButton) findViewById(R.id.rb_opC);
        rb_opD=(RadioButton) findViewById(R.id.rb_opD);
        btn_sub=(Button) findViewById(R.id.btn_submit);
        bt_done=(Button) findViewById(R.id.bt_sub);
        bt_pos1 = (Button) findViewById(R.id.bt_pos1);
        bt_pos2 = (Button) findViewById(R.id.bt_pos2);
        bt_pos3 = (Button) findViewById(R.id.bt_pos3);
        bt_pos4 = (Button) findViewById(R.id.bt_pos4);
        bt_pos5 = (Button) findViewById(R.id.bt_pos5);
        bt_pos6 = (Button) findViewById(R.id.bt_pos6);
        bt_pos7 = (Button) findViewById(R.id.bt_pos7);
        bt_pos8 = (Button) findViewById(R.id.bt_pos8);
        bt_pos9 = (Button) findViewById(R.id.bt_pos9);
        bt_pos10 = (Button) findViewById(R.id.bt_pos10);
        arrayList = new ArrayList<>();
        list_data = new ArrayList<>();
        all_quesData = new ArrayList<>();
        sharedData = new SharedPreference();
        listData1 = new ListData();


        // img_ques=(ImageView)findViewById(R.id.img_ques);
        rg_option=(RadioGroup) findViewById(R.id.rg_option);
        bt_done.setVisibility(View.GONE);

        dialog = new SpotsDialog(QuestionActivity.this, R.style.Custom);

        Intent i =  getIntent();
        tittle = i.getStringExtra("Sub");
        tv_sub.setText(tittle);
        btn_sub.setEnabled(false);
        tittle = "GeneralKnowledge";




        if (tittle.equals("TestofReasoning")){
            Intent imove = new Intent(QuestionActivity.this,ReasoingActivity.class);
            startActivity(imove);
            finish();
        }else {
            // set animation listener
            animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            animMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
            animFadein.setAnimationListener(this);
            animMove.setAnimationListener(this);
            bt_pos1.setOnClickListener(this);
            bt_pos2.setOnClickListener(this);
            bt_pos3.setOnClickListener(this);
            bt_pos4.setOnClickListener(this);
            bt_pos5.setOnClickListener(this);
            bt_pos6.setOnClickListener(this);
            bt_pos7.setOnClickListener(this);
            bt_pos8.setOnClickListener(this);
            bt_pos9.setOnClickListener(this);
            bt_pos10.setOnClickListener(this);


            //firebase database
            db_instance = FirebaseDatabase.getInstance();
            db_ref = db_instance.getReference("GeneralKnowledge");

            String qno_list = String.format("%03d",qno);
          //  Log.i("Print22",""+qno_list);
            getButton(qno_list);
            getQuestion(qno_list);

            checkedRadioButtonID = rg_option.getCheckedRadioButtonId();


                rg_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    int correctId;

                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        String ques_no;

                        btn_sub.setEnabled(true);
                        btn_sub.setBackgroundResource(R.drawable.ripple_effect);

                        rbselect = (RadioButton)group.findViewById(checkedId);
                        rbcorrect= (RadioButton)group.findViewById(checkedId);
                        checkedSelectId = group.indexOfChild(rbselect);

                        String seriesNo  = tv_quesno.getText().toString();

                        Log.i("Id77",""+checkedSelectId);
                        //Log.i("Id76",""+checkedId);

                        switch (checkedId) {
                            case R.id.rb_opA:
                                selectedAns = rb_opA.getText().toString();
                                break;
                            case R.id.rb_opB:
                                selectedAns = rb_opB.getText().toString();
                                break;
                            case R.id.rb_opC:
                                selectedAns = rb_opC.getText().toString();
                                break;
                            case R.id.rb_opD:
                                selectedAns = rb_opD.getText().toString();
                                break;

                        }

                        if (checkedSelectId!=-1) {
                            if (selectedAns.equals(correctAns)) {
                                rbselect.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                                rbcorrect = rbselect;
                                correctId = group.indexOfChild(rbcorrect);
                                Log.i("Id66", "" + correctId);
                                rb_opA.setClickable(false);
                                rb_opB.setClickable(false);
                                rb_opC.setClickable(false);
                                rb_opD.setClickable(false);
                               //((RadioGroup) findViewById(R.id.rg_option)).clearCheck();


                            } else {
                                rbselect.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.red));

                                if (rb_opA.getText().toString().equals(correctAns)) {
                                    rb_opA.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                                    rbcorrect = rb_opA;
                                    correctId = group.indexOfChild(rbcorrect);
                                    Log.i("Id66", "" + correctId);
                                } else if (rb_opB.getText().toString().equals(correctAns)) {
                                    rb_opB.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                                    rbcorrect = rb_opB;
                                    correctId = group.indexOfChild(rbcorrect);
                                    Log.i("Id66", "" + correctId);
                                } else if (rb_opC.getText().toString().equals(correctAns)) {
                                    rb_opC.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                                    rbcorrect = rb_opC;
                                    correctId = group.indexOfChild(rbcorrect);
                                    Log.i("Id66", "" + correctId);
                                } else if (rb_opD.getText().toString().equals(correctAns)) {
                                    rb_opD.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                                    rbcorrect = rb_opD;
                                    correctId = group.indexOfChild(rbcorrect);
                                    Log.i("Id66", "" + correctId);
                                }

                                //((RadioGroup) findViewById(R.id.rg_option)).clearCheck();

                                rb_opA.setClickable(false);
                                rb_opB.setClickable(false);
                                rb_opC.setClickable(false);
                                rb_opD.setClickable(false);

                            }
                            storeData(checkedSelectId, correctId,seriesNo);
                        }


                    }
                });


            btn_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_ques.startAnimation(animFadein);
                    rg_option.startAnimation(animMove);
                    checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                   int value1 = Integer.parseInt(tv_quesno.getText().toString());
                    value1++;
                   ques_noList = String.format("%03d",value1);
                   // Log.i("Print99",""+ques_noList);
                    getQuestion(ques_noList);
                    correctAns=null;
                    list_data.clear();
                    //((RadioGroup) findViewById(R.id.rg_option)).clearCheck();
                    rb_opA.setChecked(false);
                    rb_opB.setChecked(false);
                    rb_opC.setChecked(false);
                    rb_opD.setChecked(false);
                    btn_sub.setEnabled(false);

                    btn_sub.setBackgroundResource(R.color.centercolor);
                    int last_btn = Integer.parseInt(bt_pos10.getText().toString());
                    String last_btn1 = String.format("%03d",last_btn);
                    //Log.i("ButonValue11",last_btn1);
                    if (ques_noList.equals(last_btn1)){
                        getButton(last_btn1);
                    }
                }
            });

        }


    }

    private void storeData(int checkedSelectId, int correctId, String seriesNo1) {
       // list_data.add(listData);
        Log.i("seriesNO88",""+seriesNo1);

        if (all_quesData.size() == 0) {
            listData1.setQus_no(seriesNo1);
            listData1.setCorrect_data(correctId);
            listData1.setAttempted_data(checkedSelectId);
           // sharedData.addFavorite(QuestionActivity.this,listData);
           all_quesData.add(listData1);
           // Log.i("printarraylenght1", "" + all_quesData.size());
            saveData(all_quesData);
        }else {
            int last_index = all_quesData.size()-1;
            String value_lastIndx = all_quesData.get(last_index).getQus_no();
            //Log.i("Index",""+value_lastIndx);
            if (!value_lastIndx.equals(seriesNo1)){
                listData1.setQus_no(seriesNo1);
                listData1.setCorrect_data(correctId);
                listData1.setAttempted_data(checkedSelectId);
                all_quesData.add(listData1);
               saveData(all_quesData);
                //sharedData.addFavorite(QuestionActivity.this,listData);
            }

        }

       // Log.i("printarraylenght",""+all_quesData.size());

       /* for (int i= 0;i<list_data.size();i++){
            Log.i("print23",""+list_data.get(i).getAttempted_data());
            if (list_data.get(i).getAttempted_data() !=-1) {
               *//* RadioButton savedCheckedRadioButton = (RadioButton) rg_option.getChildAt(list_data.get(i).getAttempted_data());
                savedCheckedRadioButton.setChecked(true);*//*
            }
        }
*/

    }

    private void saveData(ArrayList<ListData> all_quesData) {

    }

    private void getButton(String qno_list) {


        Query query_btn = db_ref.orderByKey().startAt("Q-"+qno_list).limitToFirst(10);
        query_btn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrayList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //Log.i("key77", "" + data.getKey());
                    arrayList.add(data.getKey());

                }
                sendData(arrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendData(final ArrayList<String> arrayList) {
        String data = null,data_btn;
        int sub_data;
       // Log.i("arraydata33", "" + arrayList);
        bt_pos1.setVisibility(View.GONE);
        bt_pos2.setVisibility(View.GONE);
        bt_pos3.setVisibility(View.GONE);
        bt_pos4.setVisibility(View.GONE);
        bt_pos5.setVisibility(View.GONE);
        bt_pos6.setVisibility(View.GONE);
        bt_pos7.setVisibility(View.GONE);
        bt_pos8.setVisibility(View.GONE);
        bt_pos9.setVisibility(View.GONE);
        bt_pos10.setVisibility(View.GONE);

        for (int y = 0; y < arrayList.size(); y++) {
            switch (y) {
                case 0:
                    bt_pos1.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                     data_btn = String.format("%01d",sub_data);
                    bt_pos1.setText(data_btn);
                    break;
                case 1:
                    bt_pos2.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos2.setText(data_btn);
                    break;
                case 2:
                    bt_pos3.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos3.setText(data_btn);
                    break;
                case 3:
                    bt_pos4.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos4.setText(data_btn);
                    break;
                case 4:
                    bt_pos5.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos5.setText(data_btn);
                    break;
                case 5:
                    bt_pos6.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos6.setText(data_btn);
                    break;
                case 6:
                    bt_pos7.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos7.setText(data_btn);
                    break;
                case 7:
                    bt_pos8.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos8.setText(data_btn);
                    break;
                case 8:
                    bt_pos9.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos9.setText(data_btn);
                    break;
                case 9:
                    bt_pos10.setVisibility(View.VISIBLE);
                    data = arrayList.get(y);
                    sub_data = Integer.parseInt(data.substring(data.indexOf("-")+1,data.length()));
                    data_btn = String.format("%01d",sub_data);
                    bt_pos10.setText(data_btn);
                    break;

            }
        }


    }

    private void getQuestion(String qno){
        dialog.show();
        Query getquestion=db_ref.orderByKey().equalTo("Q-"+qno);

        getquestion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        QuestionModel qModel = child.getValue(QuestionModel.class);

                        String questype= child.getKey();
                        //Log.i("Questik1",""+questype);
                        int ques = Integer.parseInt(questype.substring(questype.indexOf("-")+1,questype.length()));
                        String sub_quesno = String.format("%01d",ques);

                        tv_quesno .setText(sub_quesno);
                            tv_ques.setText(qModel.getQuestion());
                            rb_opA.setText(qModel.getOptionA());
                            rb_opB.setText(qModel.getOptionB());
                            rb_opC.setText(qModel.getOptionC());
                            rb_opD.setText(qModel.getOptionD());
                            correctAns = qModel.getCorrect();
                            dialog.dismiss();
                    }
                }else {

                    dialog.dismiss();
                    tv_quesno.setVisibility(View.GONE);
                   tv_ques.setText("You have  Done your Test.");
                    rb_opA.setVisibility(View.INVISIBLE);
                    rb_opB.setVisibility(View.INVISIBLE);
                    rb_opC.setVisibility(View.INVISIBLE);
                    rb_opD.setVisibility(View.INVISIBLE);
                    btn_sub.setVisibility(View.GONE);
                    bt_done.setVisibility(View.VISIBLE);

                    bt_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(QuestionActivity.this,FinalMarkActivity.class);
                            intent.putExtra("tittle",tittle);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onAnimationStart(Animation animation) {

        rb_opA.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.black));
        rb_opB.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.black));
        rb_opC.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.black));
        rb_opD.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.black));

        rb_opA.setChecked(false);
        rb_opB.setChecked(false);
        rb_opC.setChecked(false);
        rb_opD.setChecked(false);
        rb_opA.setClickable(true);
        rb_opB.setClickable(true);
        rb_opC.setClickable(true);
        rb_opD.setClickable(true);

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View view) {

        String subvalue = null;
        switch (view.getId()){
            case R.id.bt_pos1:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);

                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos1.getText().toString());
                subvalue = String.format("%03d",value);

                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos2:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);

                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos2.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos3:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos3.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos4:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos4.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos5:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos5.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos6:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos6.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos7:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos7.getText().toString());
                subvalue = String.format("%03d",value); subvalue = "0"+value;
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos8:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos8.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos9:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos9.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
            case R.id.bt_pos10:
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                value = Integer.parseInt(bt_pos10.getText().toString());
                subvalue = String.format("%03d",value);
                getQuestion(subvalue);
                getButton(subvalue);
                //getQuestion(qno_list);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
                break;
        }



    }
}

