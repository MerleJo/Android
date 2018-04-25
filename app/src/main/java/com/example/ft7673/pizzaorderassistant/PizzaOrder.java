package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PizzaOrder extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, DialogInterface.OnClickListener {


    RadioGroup  rdGroup;
    RadioButton rdbtPizzeria;
    RadioButton rdbtTakeaway;
    RadioButton rdbtDelivery;
    Button      btnOrderOK;

    private int orderType = 0;          // 1= Pizzeria, 2=Takeaway, 3=Delivery

    AlertDialog alertSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_type);

        rdbtPizzeria = findViewById(R.id.rdbtPizzeria);
        rdbtTakeaway = findViewById(R.id.rdbtTakeaway);
        rdbtDelivery = findViewById(R.id.rdbtDelivery);
        rdGroup = findViewById(R.id.rdGroup);
        btnOrderOK   = findViewById(R.id.btnOrderTypeOK);

        rdGroup.setOnCheckedChangeListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnExit:
                finish();
                break;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
       return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(group.getCheckedRadioButtonId()) {
            case R.id.rdbtPizzeria:
                orderType = 1;
                break;
            case R.id.rdbtTakeaway:
                orderType = 2;
                break;
            case R.id.rdbtDelivery:
                orderType = 3;
                break;
        }
    }

    public void onClick(View v){            // funktioniert nicht
        switch(v.getId()){
            case R.id.btnOrderTypeOK:
                if(rdGroup.getCheckedRadioButtonId() != -1){
                    setContentView(R.layout.activity_main);
                }else{
                    Toast.makeText(this,R.string.toastSelectType, Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnDeliveryOK:
                // Intent für neue activity_send schreiben
                break;
            case R.id.btnCheckout:
                switch (orderType){
                    case 1:
                        //Intent für neue activitiy_send schreiben
                        break;
                    case 2:
                        //Alert Dialog mit Time Picker & Packing Option
                        break;
                    case 3:
                        setContentView(R.layout.delivery_info);
                        break;
                    default:

                }
                break;
        }
    }

    private AlertDialog selectSize(){
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertSizeTitle)
               // .setIcon()        noch einfügen

        .setSingleChoiceItems(R.array.arraySizes, -1, new DialogInterface.OnClickListener() {

        });

        alertSize = diabuilder.create();

        return alertSize;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
