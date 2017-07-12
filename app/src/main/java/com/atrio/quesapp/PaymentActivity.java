package com.atrio.quesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    Button btn_pay;
    TextView tv_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        btn_pay=(Button)findViewById(R.id.btn_pay);
        tv_amount=(TextView)findViewById(R.id.tv_amount);


    }
}
