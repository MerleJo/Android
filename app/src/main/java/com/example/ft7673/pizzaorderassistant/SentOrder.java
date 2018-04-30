package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class SentOrder extends Activity implements View.OnClickListener,
        AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {

    Button btnSendOK;
    TextView txtMoney;
    TextView txtOrderList;
    TextView txtOrderType;
    TextView firstInfo;
    TextView secondInfo;
    TextView txtTableSauce;
    ListView list;
    String   tableSauceString;
    String[] helpTitle = new String[100];
    String[] helpInfo = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        txtMoney = findViewById(R.id.txtMoney);
        //  txtOrderList = findViewById(R.id.txtOrderList);
        txtOrderType = findViewById(R.id.txtOrderType);
        firstInfo = findViewById(R.id.txtFirst);
        secondInfo = findViewById(R.id.txtSecond);
        txtTableSauce = findViewById(R.id.txtTableSauce);
        list = (ListView) findViewById(R.id.listing);

        btnSendOK = findViewById(R.id.btnSendOK);
        btnSendOK.setOnClickListener(this);

        receiveOrder();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {                                                                         // nicht nötig falls kein anderer Button noch gebraucht wird
            case R.id.btnSendOK:
                Intent intent = new Intent(this, PizzaOrder.class);                                                    // keine Bestätigung gefordert, wollen wir das machen? weil so funktioniert das leider noch nicht
                intent.putExtra("confirmation", R.string.mesConfirmation);
                setResult(R.string.resultConf, intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void receiveOrder() {
        Intent intent = getIntent();
        MyOrder order = (MyOrder) intent.getSerializableExtra("order");
        tableSauceString = order.findTableSauce();
        switch (intent.getExtras().getInt("ordertype")) {
            case 1:
                txtOrderType.setText("Option: " + getResources().getString(R.string.rdbtPizzeria).toString());
                firstInfo.setText("Table: " + String.valueOf(intent.getExtras().getInt("tableNr")));
                break;
            case 2:
                txtOrderType.setText("Option: " + getResources().getString(R.string.rdbtTakeaway).toString());
                firstInfo.setText(intent.getExtras().getString("packing"));
                secondInfo.setText(intent.getExtras().getString("time"));
                break;
            case 3:
                txtOrderType.setText("Option: " + getResources().getString(R.string.rdbtDelivery).toString());
                firstInfo.setText(intent.getExtras().getString("address"));
                secondInfo.setText(intent.getExtras().getString("phone"));
                break;
        }
        txtMoney.setText(Double.toString(order.getTotal()) + " €");
        if(!(tableSauceString.equals("none"))){
            txtTableSauce.setText("Sauce for the table: " + tableSauceString + "(" +order.getTableSaucePrice() + ")");
        }
        final ArrayList<String> listing = new ArrayList<String>();

        helpTitle = order.writeOrder();
        helpInfo = order.getMoreOrder();
        for (int i = 0; i < helpTitle.length; i++) {
            if (helpTitle[i] == null) {

            } else {
                listing.add(helpTitle[i]);
            }
        }
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listing);
        list.setAdapter(adap);
        list.setOnItemLongClickListener(this);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showOrderDetails(position);
            return true;
    }

    private AlertDialog showOrderDetails(int pos) {
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertOrderTitle)
                .setMessage(helpInfo[pos])
                .setPositiveButton(R.string.btnOK, this);

        diabuilder.create();
        return diabuilder.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i){
            case DialogInterface.BUTTON_POSITIVE:
                break;
        }
    }
}


