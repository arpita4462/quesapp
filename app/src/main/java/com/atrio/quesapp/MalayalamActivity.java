package com.atrio.quesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.atrio.quesapp.Adapter.RecycleviewAdapter;
import com.atrio.quesapp.model.ShowData;
import com.atrio.quesapp.model.UserDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class MalayalamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    File localFile;
    String geturl,currentdeviceid;
    ArrayList<ShowData> arrayList;
    ArrayList<String> arr;
    private GridLayoutManager lLayout;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    String info_data;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String userinfo = "UserKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malayalam);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_Malayalam);
        lLayout = new GridLayoutManager(MalayalamActivity.this, 2);

        recyclerView.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(lLayout);

        final SpotsDialog dialog = new SpotsDialog(MalayalamActivity.this,R.style.Custom);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference("Malayalam");

        currentdeviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dialog.show();
        try{
            checkuser();
        }catch (NullPointerException e){

            Log.i("Exception33", e.getMessage());
        }

        Query query_catlist = rootRef.child("Malayalam").child("subjectList").orderByKey();
        query_catlist.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arr = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String subkey= dataSnapshot1.getKey();
                    showimg(subkey);
                    arr.add(subkey);
//                    Log.i("array7712555",""+subkey);
                }
                if(!MalayalamActivity.this.isFinishing()) {
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void checkuser() {

        if (user == null){

            throw new NullPointerException("user is null");
        }else{
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query_realtimecheck = rootRef.child("UserDetail").orderByChild("emailId").equalTo(user.getEmail());
            Log.i("Querry66", "" + query_realtimecheck);
            query_realtimecheck.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                    String deviceid = userDetail.getDeviceId();
                    Toast.makeText(MalayalamActivity.this, "add" + deviceid, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MalayalamActivity.this, "addcurrent" + currentdeviceid, Toast.LENGTH_SHORT).show();
                    if (deviceid.equals(currentdeviceid)) {
                        Toast.makeText(MalayalamActivity.this, "add" + deviceid, Toast.LENGTH_SHORT).show();

                    } else {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MalayalamActivity.this, "addelse" + deviceid, Toast.LENGTH_SHORT).show();


                    }


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    //Toast.makeText(SubjectActivity.this,""+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(MalayalamActivity.this, "change" + currentdeviceid, Toast.LENGTH_SHORT).show();
                    UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                    String deviceid = "data";
                    deviceid =   userDetail.getDeviceId();
                    Toast.makeText(MalayalamActivity.this, "changecurrent" + deviceid, Toast.LENGTH_SHORT).show();
                    if (!deviceid.equals("data")){

                        if (deviceid.equals(currentdeviceid)) {
                            Toast.makeText(MalayalamActivity.this, "chabgeif", Toast.LENGTH_SHORT).show();
                        } else {
                            mAuth.signOut();
                            Toast.makeText(MalayalamActivity.this, "changeelse", Toast.LENGTH_SHORT).show();
                            Intent isend = new Intent(MalayalamActivity.this, LoginActivity.class);
                            isend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(isend);
                            finish();


                        }
                    }



                    //Toast.makeText(SubjectActivity.this,"change"+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MalayalamActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    private void showimg(final String subkey) {
        storageRef.child(subkey+".jpg").getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                ShowData data =  new ShowData();
                geturl=storageMetadata.getDownloadUrl().toString();
                data.setSub(subkey);
                data.setImg(geturl);
                arrayList.add(data);

                RecycleviewAdapter rcAdapter = new RecycleviewAdapter(MalayalamActivity.this, arrayList);
                recyclerView.setAdapter(rcAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ShowData data =  new ShowData();
                data.setSub(subkey);
                data.setImg("https://firebasestorage.googleapis.com/v0/b/quesapp-8d043.appspot.com/o/Subject%2Fdefaultbook.jpg?alt=media&token=c4404b07-2948-426d-8b94-dbe30cb85d2a");
                arrayList.add(data);

                RecycleviewAdapter rcAdapter = new RecycleviewAdapter(MalayalamActivity.this, arrayList);
                recyclerView.setAdapter(rcAdapter);
            }
        });


    }
}
