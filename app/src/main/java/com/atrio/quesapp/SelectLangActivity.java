package com.atrio.quesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectLangActivity extends AppCompatActivity {
    Button btn_eng,btn_malya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        btn_eng=(Button)findViewById(R.id.btn_eng);
         btn_malya=(Button)findViewById(R.id.btn_malya);

        btn_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SelectLangActivity.this,SubjectActivity.class));
            }
        });
        btn_malya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(SelectLangActivity.this,SubjectActivity.class));

            }
        });
    }
}
