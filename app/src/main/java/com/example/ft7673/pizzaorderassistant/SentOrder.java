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

    private Button   btnSendOK;
    private TextView txtMoney;                                                                      // textviews for the final layout where everything is displayed
    private TextView txtOrderType;
    private TextView firstInfo;
    private TextView secondInfo;
    private TextView txtTableSauce;
    private ListView list;                                                                          // every pizza object is displayed here
    private String   tableSauceString;
    private String[] helpTitle;                                                                     // saves the names of the pizzas
    private String[] helpInfo;                                                                      // saves the additional toppings


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

        helpTitle = new String[25];
        helpInfo  = new String[25];

        receiveOrder();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendOK:                                                                    // if pressed, Activity closes, old one opens and is reset to begin with new order. This ends the cycle
                Intent intent = new Intent(this, PizzaOrder.class);
                intent.putExtra(getResources().getString(R.string.mesConfirmation)
                        , R.string.mesConfirmation);
                setResult(R.string.resultConf, intent);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {       // long click on the item in the list to see the details of the pizza
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


    private void receiveOrder() {                                                                   // receives information of the order and displays them in this new Activity
        Intent                  intent      = getIntent();
        MyOrder                 order       = (MyOrder) intent.getSerializableExtra(getResources().getString(R.string.intentMesOrder));
        String                  help;
        final ArrayList<String> listing     = new ArrayList<String>();
        ArrayAdapter<String>    adap        = new ArrayAdapter<String>(this,
                                                android.R.layout.simple_list_item_1, listing);
        tableSauceString                    = order.findTableSauce();

        switch (intent.getExtras().getInt(getResources().getString(R.string.intentMesOrderType))) {                                           // depending on the order type the received information are displayed
            case 1:
                txtOrderType.setText(getResources().getString(R.string.stringPartOption) +" " +
                        getResources().getString(R.string.rdbtPizzeria).toString());
                firstInfo.setText(getResources().getString(R.string.stringPartTable) + " " +
                        String.valueOf(intent.getExtras().getInt(getResources()
                                .getString(R.string.intentMesTable))));
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
        help = String.format("%.2f", order.getTotal());
        txtMoney.setText(help + getResources()
                .getString(R.string.currency));

        if(!(tableSauceString.equals(getResources().getString(R.string.stringNone)))){
            help = String.format("%.2f", order.getTableSaucePrice());
            txtTableSauce.setText(getResources().getString(R.string.stringPartSauce)+" "
                    + tableSauceString + "(" + help+ getResources().getString(R.string.currency) +" )");
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


    private AlertDialog showOrderDetails(int pos) {                                                 // shows the details of the pizza.
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder  .setTitle(R.string.alertOrderTitle)
                    .setMessage(helpInfo[pos])
                    .setPositiveButton(R.string.btnOK, this)
                    .setIcon(R.drawable.cart);
        diabuilder.create();
        return diabuilder.show();
    }
}


