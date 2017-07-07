package com.atrio.quesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atrio.quesapp.model.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dmax.dialog.SpotsDialog;

public class QuestionActivity extends AppCompatActivity implements Animation.AnimationListener {

    RadioGroup rg_option;
    RadioButton rb_opA,rb_opB,rb_opC,rb_opD,rbselect,rbcorrect;
    Button btn_sub,bt_done;
    TextView tv_sub,tv_ques;
    ImageView img_ques;
    Animation animFadein,animMove;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    public String tittle,correctAns,selectedAns,seriesNo;
    int qno=1,correctValue =0,checkedRadioButtonID,total_question=0;
    SpotsDialog dialog;
    private FirebaseStorage storage;

    private StorageReference storageRef;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Value = "correct_value";

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        tv_sub=(TextView)findViewById(R.id.tv_sub);
        tv_ques=(TextView)findViewById(R.id.tv_ques);
        rb_opA=(RadioButton) findViewById(R.id.rb_opA);
        rb_opB=(RadioButton) findViewById(R.id.rb_opB);
        rb_opC=(RadioButton) findViewById(R.id.rb_opC);
        rb_opD=(RadioButton) findViewById(R.id.rb_opD);
        btn_sub=(Button) findViewById(R.id.btn_submit);
        bt_done=(Button) findViewById(R.id.bt_sub);
        img_ques=(ImageView)findViewById(R.id.img_ques);
        rg_option=(RadioGroup) findViewById(R.id.rg_option);
        bt_done.setVisibility(View.GONE);

        dialog = new SpotsDialog(QuestionActivity.this, R.style.Custom);

        Intent i =  getIntent();
        tittle = i.getStringExtra("tittle");
        seriesNo = i.getStringExtra("SeriesNo");
        tv_sub.setText(tittle);
        btn_sub.setEnabled(false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // set animation listener
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);
        animFadein.setAnimationListener(this);
        animMove.setAnimationListener(this);

        //firebase storage
        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        //firebase database
        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference(tittle);
        getQuestion(qno);

        checkedRadioButtonID = rg_option.getCheckedRadioButtonId();

        if (checkedRadioButtonID ==-1){

            rg_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                    btn_sub.setEnabled(true);
                    btn_sub.setBackgroundResource(R.drawable.ripple_effect);

                    rbselect = (RadioButton)group.findViewById(checkedId);
                    rbcorrect= (RadioButton)group.findViewById(checkedId);

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

                if (selectedAns.equals(correctAns)){

                    rbselect.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                    rbcorrect=rbselect;
                    rb_opA.setClickable(false);
                    rb_opB.setClickable(false);
                    rb_opC.setClickable(false);
                    rb_opD.setClickable(false);
                    correctValue++;

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putInt(Value, correctValue);
                    editor.commit();

                }else {
                    rbselect.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.red));
                    if(rb_opA.getText().toString().equals(correctAns)){
                        rb_opA.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                        rbcorrect=rb_opA;
                    }else  if(rb_opB.getText().toString().equals(correctAns)){
                        rb_opB.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                        rbcorrect=rb_opB;
                    }else if(rb_opC.getText().toString().equals(correctAns)){
                        rb_opC.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                        rbcorrect=rb_opC;
                    }else if(rb_opD.getText().toString().equals(correctAns)){
                        rb_opD.setTextColor(ContextCompat.getColor(QuestionActivity.this, R.color.green));
                        rbcorrect=rb_opD;
                    }

                    rb_opA.setClickable(false);
                    rb_opB.setClickable(false);
                    rb_opC.setClickable(false);
                    rb_opD.setClickable(false);

                }


                }
            });
        }

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                total_question++;
                checkedRadioButtonID =rg_option.getCheckedRadioButtonId();
                qno++;
                getQuestion(qno);
                correctAns=null;
                rb_opA.setChecked(false);
                rb_opB.setChecked(false);
                rb_opC.setChecked(false);
                rb_opD.setChecked(false);
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
            }
        });

    }

    private void getQuestion(int qno){
        dialog.show();

        Query getquestion=db_ref.child(seriesNo).orderByKey().equalTo("Q-"+qno);

        getquestion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        QuestionModel qModel = child.getValue(QuestionModel.class);
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
                   tv_ques.setText("You have  Done your Test.");
//                    img_ques.setVisibility(View.INVISIBLE);
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
                            intent.putExtra("value",correctValue);
                            intent.putExtra("Total",total_question);
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
}

