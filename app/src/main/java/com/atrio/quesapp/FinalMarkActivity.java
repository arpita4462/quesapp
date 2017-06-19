package com.atrio.quesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class FinalMarkActivity extends AppCompatActivity {
    long question;
    int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_mark);
        Intent intent = getIntent();
        question = intent.getLongExtra("Total",0);
         value = intent.getIntExtra("value",0);
        Log.i("quest88",""+question);
        Log.i("quest89",""+value);



    }
}
