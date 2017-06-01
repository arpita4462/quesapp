package com.atrio.quesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SubjectActivity extends AppCompatActivity {
    Button btn_maths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        btn_maths=(Button)findViewById(R.id.btn_math);



        btn_maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SubjectActivity.this,"Hello",Toast.LENGTH_SHORT).show();
                Log.i("hello","mathsbtn");
            }
        });
    }
}
