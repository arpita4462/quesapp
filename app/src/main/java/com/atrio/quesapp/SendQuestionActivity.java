package com.atrio.quesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    ArrayList<String> data;
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

                    if (!sub.equals("UserDetail")){
                        data.add(child.getKey());

                    }


                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, data);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
