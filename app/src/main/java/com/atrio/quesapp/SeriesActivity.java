package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class SeriesActivity extends AppCompatActivity {

    TextView tv_subject,tv_seriesNo;
    ListView lv_series;
    String tittle,seriesNo;
    DatabaseReference m_db;
    FirebaseListAdapter<Object> mAdapter;
    ArrayList<String> arrayseries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        tv_subject=(TextView)findViewById(R.id.tv_subject);
        lv_series=(ListView) findViewById(R.id.listview);

        Intent i =  getIntent();
        tittle = i.getStringExtra("Sub");
        tv_subject.setText(tittle);
        arrayseries = new ArrayList<>();
        m_db = FirebaseDatabase.getInstance().getReference();

        final SpotsDialog dialog = new SpotsDialog(SeriesActivity.this,R.style.Custom);
        dialog.show();

        Query query_listview = m_db.child(tittle).orderByKey();
        query_listview.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String subSeries= dataSnapshot1.getKey();
                    arrayseries.add(subSeries);
                    ArrayAdapter adapter = new ArrayAdapter<String>(SeriesActivity.this,R.layout.custom_series,R.id.tv_series,arrayseries);
                    lv_series.setAdapter(adapter);

                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        lv_series.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tv_seriesNo=(TextView)view.findViewById(R.id.tv_series);
                seriesNo=tv_seriesNo.getText().toString();
                if (tittle.equals("TestofResoning")){
                    Intent intent = new Intent(view.getContext(), ReasoingActivity.class);
                    intent.putExtra("SeriesNo",seriesNo);
                    intent.putExtra("tittle",tittle);
                    view.getContext().startActivity(intent);
                }else {
                    Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                    intent.putExtra("SeriesNo", seriesNo);
                    intent.putExtra("tittle", tittle);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }
}
