package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

public class QuestionActivity extends AppCompatActivity implements Animation.AnimationListener {

    RadioGroup rg_option;
    RadioButton rb_opA,rb_opB,rb_opC,rb_opD;
    Button btn_sub,bt_done;
    TextView tv_sub,tv_ques;
    Animation animFadein,animMove;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    public String tittle,correctAns,selectedAns;
    int qno=1;
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
        rg_option=(RadioGroup) findViewById(R.id.rg_option);
        bt_done.setVisibility(View.GONE);

        Intent i =  getIntent();
        tittle = i.getStringExtra("Sub");
        tv_sub.setText(tittle);
        btn_sub.setEnabled(false);

        // set animation listener
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);
        animFadein.setAnimationListener(this);
        animMove.setAnimationListener(this);

        //firebase database
        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("GeneralKnowledge");
        getQuestion(qno);
        db_ref = db_instance.getReference(tittle);
        getQuestion(qno);

//        Log.i("correctans",""+correctAns);

        rg_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                btn_sub.setEnabled(true);
                btn_sub.setBackgroundResource(R.drawable.ripple_effect);
                switch (checkedId){
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
                    default:
                        // Your code
                        break;
                }

                Log.i("selectedans",""+selectedAns);
                Log.i("correctans2",""+correctAns);

                if (selectedAns.equals(correctAns)){
//                    rg_option.getCheckedRadioButtonId()
                }
            }
        });

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ques.startAnimation(animFadein);
                rg_option.startAnimation(animMove);
                qno++;
                getQuestion(qno);
                rg_option.clearCheck();
                btn_sub.setEnabled(false);
                btn_sub.setBackgroundResource(R.color.centercolor);
            }
        });

    }

    private void getQuestion(int qno){
        Query getquestion=db_ref.orderByKey().equalTo("Q-"+qno);

        getquestion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.i("getdata2", "" + dataSnapshot.toString());
                        QuestionModel qModel = child.getValue(QuestionModel.class);

                        tv_ques.setText(qModel.getQuestion());
                        rb_opA.setText(qModel.getOptionA());
                        rb_opB.setText(qModel.getOptionB());
                        rb_opC.setText(qModel.getOptionC());
                        rb_opD.setText(qModel.getOptionD());
                        correctAns=qModel.getCorrect();
                    }
                }else {
                    btn_sub.setText("Submit");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
