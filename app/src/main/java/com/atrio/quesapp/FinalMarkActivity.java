package com.atrio.quesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.atrio.quesapp.model.ListData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FinalMarkActivity extends AppCompatActivity {
    TextView tv_ques,tv_correct,tv_quote;
    ImageView img;
    double ans,correctans,totalques;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    String tittle;
    long total_data;
    int value;

//    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_mark);
        tv_ques=(TextView)findViewById(R.id.tv_ques);
        tv_correct=(TextView)findViewById(R.id.tv_correct);
        img=(ImageView) findViewById(R.id.img_emoji);
        tv_quote=(TextView)findViewById(R.id.tv_quote);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(FinalMarkActivity.this);
       /* Gson gson = new Gson();
        String json = sharedPrefs.getString("Data", null);
        Type type = new TypeToken<ArrayList<ListData>>() {}.getType();
        ArrayList<ListData> arrayList = gson.fromJson(json, type);
*/

        //Log.i("PrintData77",""+arrayList);



        Intent intent = getIntent();
        tittle = intent.getStringExtra("tittle");



        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference(tittle);

        Query getquestion=db_ref.orderByKey();

        getquestion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("total44",""+dataSnapshot.getChildrenCount());
                total_data = dataSnapshot.getChildrenCount();
                tv_ques.setText("Total No of Question: "+total_data);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        tv_correct.setText("Total No of Correct Answer: "+value);

       /* correctans=value;
        totalques=question;*/

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
