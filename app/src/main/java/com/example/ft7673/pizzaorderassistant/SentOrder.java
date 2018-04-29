package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SentOrder extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    Button btnSendOK;
    TextView txtMoney;
    TextView txtOrderList;
    TextView txtOrderType;
    TextView firstInfo;
    TextView secondInfo;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        txtMoney = findViewById(R.id.txtMoney);
      //  txtOrderList = findViewById(R.id.txtOrderList);
        txtOrderType = findViewById(R.id.txtOrderType);
        firstInfo = findViewById(R.id.txtFirst);
        secondInfo = findViewById(R.id.txtSecond);
        list = (ListView)findViewById(R.id.listing);

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
        MyOrder order = (MyOrder)intent.getSerializableExtra("order");                     // brauchen wir Seriablizable wirklich?
        switch (intent.getExtras().getInt("ordertype")){
            case 1:
               txtOrderType.setText(R.string.rdbtPizzeria);
                break;
            case 2:
                txtOrderType.setText(R.string.rdbtTakeaway);
                firstInfo.setText(intent.getExtras().getString("packing"));
                secondInfo.setText(intent.getExtras().getString("time"));
                break;
            case 3:
                txtOrderType.setText(R.string.rdbtDelivery);
                firstInfo.setText(intent.getExtras().getString("address"));
                secondInfo.setText(intent.getExtras().getString("phone"));
                break;
        }
        txtMoney.setText(Double.toString(order.getTotal()));
        final ArrayList<String> listing = new ArrayList<String>();
        String [] help = new String[100];
        help = order.writeOrder();
        for (int i = 0; i < help.length; i++) {
            if (help[i] == null) {

            } else {
                listing.add(help[i]);
            }
        }
        //listing.add(order.writeOrder());
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listing);
        list.setAdapter(adap);
        list.setOnItemLongClickListener(this);      //noch Logik schreiben

        //txtOrderList.setText(order.writeOrder());
       // txtOrderList.setText(Arrays.toString(order.getOrder()).replaceAll("\\[|\\]", ""));


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}


