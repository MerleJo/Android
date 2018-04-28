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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        etTotal = findViewById(R.id.etTotal);
        etOrder = findViewById(R.id.etOrder);

        btnSendOK = findViewById(R.id.btnSendOK);
        btnSendOK.setOnClickListener(this);

        //receiveOrder();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){                                                                         // nicht nötig falls kein anderer Button noch gebraucht wird
            case R.id.btnSendOK:
                //Intent intent = new Intent();                                                     keine Bestätigung gefordert, wollen wir das machen? weil so funktioniert das leider noch nicht
               // intent.putExtra(R.string.mesConfirmation,);
                setContentView(R.layout.order_type);
                // welche Dinge noch zurücksetzen?
                break;
            default:
                break;
        }
    }

   /* private void receiveOrder(){
        Intent intent = getIntent();
        MyOrder order = new MyOrder(intent.getDoubleExtra(), intent.getStringArrayExtra());         // funktioniert nicht, solang ich nicht weiß, was wir mitgeben

        etTotal.setText(Double.toString(order.getTotal()));
        etOrder.setText(Arrays.toString(order.getOrder()).replaceAll("\\[|\\]", ""));
*/


    }

