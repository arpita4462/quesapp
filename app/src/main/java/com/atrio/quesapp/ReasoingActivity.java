package com.atrio.quesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atrio.quesapp.model.QuestionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import dmax.dialog.SpotsDialog;

public class ReasoingActivity extends AppCompatActivity implements Animation.AnimationListener {

    RadioGroup rg_option;
    RadioButton rb_opA,rb_opB,rb_opC,rb_opD,rbselect,rbcorrect;
    Button btn_sub,bt_done;
    TextView tv_sub,tv_ques;
    ImageView img_ques;
    Animation animFadein,animMove;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    public String tittle,correctAns,selectedAns,seriesNo;
    int qno=1,correctValue =0,checkedRadioButtonID,total_question=0;
    SpotsDialog dialog;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    File localFile;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Value = "correct_value";
    SharedPreferences sharedpreferences;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasoing);
        tv_sub=(TextView)findViewById(R.id.tv_sub);
        tv_ques=(TextView)findViewById(R.id.tv_ques);
        rb_opA=(RadioButton) findViewById(R.id.rb_opA);
        rb_opB=(RadioButton) findViewById(R.id.rb_opB);
        rb_opC=(RadioButton) findViewById(R.id.rb_opC);
        rb_opD=(RadioButton) findViewById(R.id.rb_opD);
        btn_sub=(Button) findViewById(R.id.btn_submit);
        bt_done=(Button) findViewById(R.id.bt_sub);
        img_ques=(ImageView)findViewById(R.id.img_ques);
        rg_option=(RadioGroup) findViewById(R.id.rg_option);
        bt_done.setVisibility(View.GONE);

        dialog = new SpotsDialog(ReasoingActivity.this, R.style.Custom);

        Intent i =  getIntent();
        tittle = i.getStringExtra("tittle");
        seriesNo = i.getStringExtra("SeriesNo");
        tv_sub.setText(seriesNo);
        btn_sub.setEnabled(false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // set animation listener
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);
        animFadein.setAnimationListener(this);
        animMove.setAnimationListener(this);

        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference("Reasoning");

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("TestofReasoning");

        getQuestion();

        checkedRadioButtonID = rg_option.getCheckedRadioButtonId();
        
        
    }

    private void getQuestion() {
        Query getquestion=db_ref.child(seriesNo).orderByKey().equalTo("Q-"+qno);

        getquestion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() !=0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        QuestionModel qModel = child.getValue(QuestionModel.class);
                        
                        String questype= qModel.getQuestion();
                        String gettext=questype.substring(0,questype.indexOf("mg-"));
                        String getimg = questype.substring(questype.indexOf("-")+1);
                        Log.i("imgname",""+getimg);
//                            Picasso.with(ReasoingActivity.this).load(geturl).into(img_ques);

                            showimg(getimg);
                            tv_ques.setText(gettext);
                            rb_opA.setText(qModel.getOptionA());
                            rb_opB.setText(qModel.getOptionB());
                            rb_opC.setText(qModel.getOptionC());
                            rb_opD.setText(qModel.getOptionD());
                            correctAns=qModel.getCorrect();
//                            dialog.dismiss();
                    }
                }else {
//                    dialog.dismiss();
                    tv_ques.setText("You have  Done your Test.");
                    img_ques.setVisibility(View.INVISIBLE);
                    rb_opA.setVisibility(View.INVISIBLE);
                    rb_opB.setVisibility(View.INVISIBLE);
                    rb_opC.setVisibility(View.INVISIBLE);
                    rb_opD.setVisibility(View.INVISIBLE);
                    btn_sub.setVisibility(View.GONE);
                    bt_done.setVisibility(View.VISIBLE);

                    bt_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ReasoingActivity.this,FinalMarkActivity.class);
                            intent.putExtra("value",correctValue);
                            intent.putExtra("Total",total_question);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showimg(String getimg) {
/*
        storageRef.child(getimg).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.i("imagename",""+storageMetadata.getName());

            }
        });
*/
        try {

            localFile = File.createTempFile("images", "png");

            storageRef.child(getimg).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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

    }

    @Override
    public void onAnimationStart(Animation animation) {
        rb_opA.setTextColor(ContextCompat.getColor(ReasoingActivity.this, R.color.black));
        rb_opB.setTextColor(ContextCompat.getColor(ReasoingActivity.this, R.color.black));
        rb_opC.setTextColor(ContextCompat.getColor(ReasoingActivity.this, R.color.black));
        rb_opD.setTextColor(ContextCompat.getColor(ReasoingActivity.this, R.color.black));

        rb_opA.setChecked(false);
        rb_opB.setChecked(false);
        rb_opC.setChecked(false);
        rb_opD.setChecked(false);

        rb_opA.setClickable(true);
        rb_opB.setClickable(true);
        rb_opC.setClickable(true);
        rb_opD.setClickable(true);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
