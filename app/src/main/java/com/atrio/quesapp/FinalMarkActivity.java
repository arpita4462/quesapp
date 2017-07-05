package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class FinalMarkActivity extends AppCompatActivity {
    TextView tv_ques,tv_correct,tv_quote;
    ImageView img;
    int value,question;
    double ans,correctans,totalques;
//    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_mark);
        tv_ques=(TextView)findViewById(R.id.tv_ques);
        tv_correct=(TextView)findViewById(R.id.tv_correct);
        img=(ImageView) findViewById(R.id.img_emoji);
        tv_quote=(TextView)findViewById(R.id.tv_quote);



        Intent intent = getIntent();
        question = intent.getIntExtra("Total",0);
         value = intent.getIntExtra("value",0);

        tv_ques.setText("Total No of Question: "+question);
        tv_correct.setText("Total No of Correct Answer: "+value);

        correctans=value;
        totalques=question;

        ans=(correctans/totalques)*100;
        if (ans==100){
            img.setBackgroundResource(R.drawable.clapimg);
            tv_quote.setText("Congratulation....");
        }else if(ans<50){
            img.setBackgroundResource(R.drawable.sadoutimg);
            tv_quote.setText("Keep Trying ...Better next luck....");
        }else {
            img.setBackgroundResource(R.drawable.happyoutimg);
            tv_quote.setText("Best Wishes....Try again....");
        }




    }
}
