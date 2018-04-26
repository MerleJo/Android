package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SentOrder extends Activity implements View.OnClickListener {

    Button btnSendOK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        btnSendOK = findViewById(R.id.btnSendOK);
        btnSendOK.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSendOK:
                setContentView(R.layout.order_type);
                // welche Dinge noch zurücksetzen?
                break;
            default:
                break;
        }
    }
}
