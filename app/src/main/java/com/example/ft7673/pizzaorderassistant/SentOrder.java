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

        txtMoney        = findViewById(R.id.txtMoney);
        txtOrderType    = findViewById(R.id.txtOrderType);
        firstInfo       = findViewById(R.id.txtFirst);
        secondInfo      = findViewById(R.id.txtSecond);
        txtTableSauce   = findViewById(R.id.txtTableSauce);
        list            = findViewById(R.id.listing);
        btnSendOK       = findViewById(R.id.btnSendOK);

        btnSendOK.setOnClickListener(this);

        receiveOrder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendOK:
                Intent intent = new Intent(this, PizzaOrder.class);
                intent.putExtra(getResources().getString(R.string.mesConfirmation), R.string.mesConfirmation);
                setResult(R.string.resultConf, intent);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showOrderDetails(position);
        return true;
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i){
            case DialogInterface.BUTTON_POSITIVE:
                break;
        }
    }


    private void receiveOrder() {
        Intent intent                       = getIntent();
        MyOrder order                       = (MyOrder) intent.getSerializableExtra(getResources().getString(R.string.intentMesOrder));
        tableSauceString                    = order.findTableSauce();
        final ArrayList<String> listing     = new ArrayList<String>();
        ArrayAdapter<String> adap           = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listing);

        switch (intent.getExtras().getInt(getResources().getString(R.string.intentMesOrderType))) {
            case 1:
                txtOrderType.setText(getResources().getString(R.string.stringPartOption) +" " +
                        getResources().getString(R.string.rdbtPizzeria).toString());
                firstInfo.setText(getResources().getString(R.string.stringPartTable) +
                        String.valueOf(intent.getExtras().getInt(getResources().getString(R.string.intentMesTable))));
                break;
            case 2:
                txtOrderType.setText(getResources().getString(R.string.stringPartOption)+ " " +
                        getResources().getString(R.string.rdbtTakeaway).toString());
                firstInfo.setText(intent.getExtras().getString(getResources().getString(R.string.intentMesPacking)));
                secondInfo.setText(intent.getExtras().getString(getResources().getString(R.string.intentMesTime)));
                break;
            case 3:
                txtOrderType.setText(getResources().getString(R.string.stringPartOption)+ " " +
                        getResources().getString(R.string.rdbtDelivery).toString());
                firstInfo.setText(intent.getExtras().getString(getResources().getString(R.string.intentMesAddress)));
                secondInfo.setText(intent.getExtras().getString(getResources().getString(R.string.intentMesPhone)));
                break;
        }
        txtMoney.setText(Double.toString(order.getTotal()) + " €");
        if(!(tableSauceString.equals(getResources().getString(R.string.stringNone)))){
            txtTableSauce.setText(getResources().getString(R.string.stringPartSauce)+" " +
                    tableSauceString + "(" +order.getTableSaucePrice() + " € )");
        }

        helpTitle   = order.writeOrder();
        helpInfo    = order.getMoreOrder();
        for (int i = 0; i < helpTitle.length; i++) {
            if (helpTitle[i] != null) {
                listing.add(helpTitle[i]);
            }
        }
        list.setAdapter(adap);
        list.setOnItemLongClickListener(this);
    }


    private AlertDialog showOrderDetails(int pos) {
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder  .setTitle(R.string.alertOrderTitle)
                    .setMessage(helpInfo[pos])
                    .setPositiveButton(R.string.btnOK, this)
                    .setIcon(R.drawable.cart);
        diabuilder.create();
        return diabuilder.show();
    }
}


