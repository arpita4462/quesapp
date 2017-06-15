package com.atrio.quesapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.atrio.quesapp.Adapter.RecycleviewAdapter;
import com.atrio.quesapp.model.ShowData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class SubjectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ShowData> arrayList;
    private GridLayoutManager lLayout;
    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        lLayout = new GridLayoutManager(SubjectActivity.this, 2);

        recyclerView.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(lLayout);

        final SpotsDialog dialog = new SpotsDialog(SubjectActivity.this,R.style.Custom);
        dialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Subject");
        Query  query_catlist = rootRef.orderByKey();
        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    ShowData data =  dataSnapshot1.getValue(ShowData.class);
                   /* Log.i("Sub22",data.getSub());
                    Log.i("Img33",data.getImg());*/
                    data.setImg(data.getImg());
                    data.setSub(data.getSub());
                    arrayList.add(data);

                }

                dialog.dismiss();
                RecycleviewAdapter rcAdapter = new RecycleviewAdapter(SubjectActivity.this, arrayList);
                recyclerView.setAdapter(rcAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
