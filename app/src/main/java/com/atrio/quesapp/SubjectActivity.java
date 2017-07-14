package com.atrio.quesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.atrio.quesapp.Adapter.RecycleviewAdapter;
import com.atrio.quesapp.model.ShowData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class SubjectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    File localFile;
    String geturl;
    ArrayList<ShowData> arrayList;
    private GridLayoutManager lLayout;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    Button bt_ques;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        bt_ques = (Button) findViewById(R.id.bt_question);
        lLayout = new GridLayoutManager(SubjectActivity.this, 2);

        recyclerView.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(lLayout);

        final SpotsDialog dialog = new SpotsDialog(SubjectActivity.this,R.style.Custom);
        dialog.show();

        bt_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(SubjectActivity.this,SendQuestionActivity.class);
                startActivity(move);
            }
        });
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference("Subject");
        Query query_catlist = rootRef.orderByKey();
        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String subkey= dataSnapshot1.getKey();
                    showimg(subkey);
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void showimg(final String sub) {
        storageRef.child(sub+".jpg").getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                ShowData data =  new ShowData();
                geturl=storageMetadata.getDownloadUrl().toString();
                data.setSub(sub);
                data.setImg(geturl);
                arrayList.add(data);
                Log.i("data7712",""+data);


                RecycleviewAdapter rcAdapter = new RecycleviewAdapter(SubjectActivity.this, arrayList);
                recyclerView.setAdapter(rcAdapter);

            }
        });
    }
}