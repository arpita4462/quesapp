package com.atrio.quesapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atrio.quesapp.MultipleChoiceActivity;
import com.atrio.quesapp.QuestionAnswerActivity;
import com.atrio.quesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 07-11-2017.
 */

public class CustomFabDialog extends Dialog{
    Context mycontext;
    Button btn_cancel,btn_go;
    EditText et_quesno;
    TextInputLayout inputLayoutName;
    private ProgressBar progressBar;
    String  ques_no,tittle,lang;

    public CustomFabDialog(Context context, String tittle, String lang) {
        super(context);
        mycontext=context;
        this.tittle =tittle;
        this.lang = lang;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        setContentView(R.layout.custom_fab_dialog);

        et_quesno = (EditText) findViewById(R.id.et_quesno);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_go = (Button) findViewById(R.id.btn_go);
       // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputLayoutName = (TextInputLayout)findViewById(R.id.input_layout_frg_id);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quesno = et_quesno.getText().toString().trim();

                if (TextUtils.isEmpty(quesno)) {
                    inputLayoutName.setError(getContext().getString(R.string.enterquesnofield));
                    return;
                }else{
                    if (tittle.equals("MultipleChoiceQuestion")){

                        ques_no = et_quesno.getText().toString();
                        Intent intent = new Intent(mycontext,MultipleChoiceActivity.class);
                        intent.putExtra("ques_no",ques_no);
                        intent.putExtra("Sub",tittle);
                        intent.putExtra("lang",lang);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mycontext.startActivity(intent);
                        dismiss();

                    }else{
                        ques_no = et_quesno.getText().toString();
                        Intent intent = new Intent(mycontext,QuestionAnswerActivity.class);
                        intent.putExtra("ques_no",ques_no);
                        intent.putExtra("Sub",tittle);
                        intent.putExtra("lang",lang);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mycontext.startActivity(intent);
                        dismiss();
                    }

                }



            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });


    }

}
