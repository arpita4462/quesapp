package com.atrio.quesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
    ArrayList<ShowData> arrayList;
    private GridLayoutManager lLayout;
    private FirebaseStorage storage;
    private StorageReference storageRef;
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

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference("Subject");
        Query query_catlist = rootRef.orderByKey();
        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    Log.i("subject56",dataSnapshot1.getKey());
//                    showimg(getimg);
                    final ShowData data =  new ShowData();
//                    data.setImg(data.getImg());
                    data.setSub(dataSnapshot1.getKey());
                    Log.i("subject564",data.getSub());

                    storageRef.child(data.getSub()).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            Log.i("imagename",""+storageMetadata.getPath());
                            data.setImg(storageMetadata.getPath());


                        }
                    });

/*
                    storageRef.child(data.getSub()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i("subject56",uri.toString());

                            data.setImg(uri.toString());

                            // Got the download URL for 'users/me/profile.png'
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
*/
/*
                    try {

                        localFile = File.createTempFile("images", "png");

                        storageRef.child(data.getSub()).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                img_ques.setImageBitmap(bitmap);
                                Log.i("storageimg",""+bitmap);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
*/


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