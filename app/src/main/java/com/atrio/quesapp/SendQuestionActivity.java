package com.atrio.quesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.atrio.quesapp.Adapter.CustomAdapter;
import com.atrio.quesapp.model.CustomSpinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendQuestionActivity extends AppCompatActivity {

    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    ArrayList<CustomSpinner> data;
    CustomSpinner spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_question);
        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference();
        Query getsub=db_ref.orderByKey();

        data = new ArrayList<>();


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        getsub.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String sub = child.getKey();
                    spin = new CustomSpinner();
                    if (!sub.equals("UserDetail")){
                        spin.setSub(child.getKey());
                        Log.i("data77",""+sub);
                        data.add(spin);
                    }


                }
                Log.i("data77",""+data.size());
                CustomAdapter customAdapter=new CustomAdapter(SendQuestionActivity.this,data);
                spinner.setAdapter(customAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
