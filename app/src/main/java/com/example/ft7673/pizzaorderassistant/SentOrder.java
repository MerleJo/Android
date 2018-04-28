package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;


public class SentOrder extends Activity implements View.OnClickListener {

    Button btnSendOK;
    EditText etTotal;
    EditText etOrder;
    EditText etOrderType;
    EditText firstInfo;
    EditText secondInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        etTotal = findViewById(R.id.etTotal);
        etOrder = findViewById(R.id.etOrder);
        etOrderType = findViewById(R.id.etOrderType);
        firstInfo = findViewById(R.id.etFirst);
        secondInfo = findViewById(R.id.etSecond);

        btnSendOK = findViewById(R.id.btnSendOK);
        btnSendOK.setOnClickListener(this);

        receiveOrder();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){                                                                         // nicht nötig falls kein anderer Button noch gebraucht wird
            case R.id.btnSendOK:
                Intent intent = new Intent();                                                    // keine Bestätigung gefordert, wollen wir das machen? weil so funktioniert das leider noch nicht
               intent.putExtra("confirmation", R.string.mesConfirmation);
                setContentView(R.layout.order_type);
                // welche Dinge noch zurücksetzen?
                break;
            default:
                break;
        }
    }

    private void receiveOrder() {
        Intent intent = getIntent();
        MyOrder order = new MyOrder((Pizza[]) intent.getExtras().get("order"));                     // brauchen wir Seriablizable wirklich?
        switch (intent.getExtras().getInt("ordertype")){
            case 1:
               etOrderType.setText(R.string.rdbtPizzeria);
                break;
            case 2:
                etOrderType.setText(R.string.rdbtTakeaway);
                firstInfo.setText(intent.getExtras().getString("packing"));
                secondInfo.setText(intent.getExtras().getString("time"));
                break;
            case 3:
                etOrderType.setText(R.string.rdbtDelivery);
                firstInfo.setText(intent.getExtras().getString("address"));
                secondInfo.setText(intent.getExtras().getString("phone"));
                break;
        }
        etTotal.setText(Double.toString(order.getTotal()));
        etOrder.setText(Arrays.toString(order.getOrder()).replaceAll("\\[|\\]", ""));


    }
}

