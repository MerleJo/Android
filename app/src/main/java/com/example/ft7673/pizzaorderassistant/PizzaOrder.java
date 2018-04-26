package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class PizzaOrder extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {


    RadioGroup                      rdGroup;
    RadioButton                     rdbtPizzeria;
    RadioButton                     rdbtTakeaway;
    RadioButton                     rdbtDelivery;
    Button                          btnOrderOK;
    Button                          btnPickupOK;

    Button                          btnCheckout;
    Button                          btnAdd;
    Button                          btnDeliveryOK;

    private Spinner                 spPizza;
    private Spinner                 spDough;
    private Spinner                 spSize;
    private Spinner                 spTopp;
    private Spinner                 spSauce;

    private ArrayAdapter<String>    adprPizza;

    private int                     orderType = 0;                                                      // 1= Pizzeria, 2=Takeaway, 3=Delivery
    private int                     cntPiz;
    AlertDialog                     alertSize;
    AlertDialog                     alertTake;

    private InputStream             inputStream;
    private String[]                output;
    private String                  lineReader;
    private String                  delivaddress;
    private String                  delivphone;
    private BufferedReader          reader;

    private String[]                pizzaList;

    private Calendar                calendar;

    private EditText                etPickedTime;
    private EditText                etPacking;
    private EditText                etAddress;
    private EditText                etPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_type);


        rdGroup      = findViewById(R.id.rdGroup);
        rdbtPizzeria = findViewById(R.id.rdbtPizzeria);
        rdbtTakeaway = findViewById(R.id.rdbtTakeaway);
        rdbtDelivery = findViewById(R.id.rdbtDelivery);

        btnOrderOK   = findViewById(R.id.btnOrderTypeOK);
        btnOrderOK.setOnClickListener(this);

        rdGroup.setOnCheckedChangeListener(this);

        pizzaList   = new String[15];

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

            default:
                // Benötigt?
        }
    }

    public void defineOrder(){
        setContentView(R.layout.activity_main);

        spPizza     = findViewById(R.id.spPizza);
        spDough     = findViewById(R.id.spDough);
        spSize      = findViewById(R.id.spSize);
        spTopp      = findViewById(R.id.spTopp);
        spSauce     = findViewById(R.id.spSauce);
        //spPizza.setPromptId(R.string.hintPizza); need a hint

        btnAdd      = findViewById(R.id.btnAdd);
        btnCheckout = findViewById(R.id.btnCheckout);

        btnAdd.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);

        readFile();
        adprPizza = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pizzaList);
        adprPizza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPizza.setAdapter(adprPizza);
        spPizza.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v){            // funktioniert nicht
        switch(v.getId()){
            case R.id.btnOrderTypeOK:
                if(rdGroup.getCheckedRadioButtonId() != -1){
                    defineOrder();


                }else{
                    Toast.makeText(this,R.string.toastSelectType, Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnAdd:


                break;

            case R.id.btnCheckout:
                switch (orderType){
                    case 1:
                        //Intent für neue activitiy_send schreiben
                        break;
                    case 2:
                        setContentView(R.layout.pickup_info);
                        etPickedTime = findViewById(R.id.etPickedTime);
                        etPacking = findViewById(R.id.etPacking);
                        btnPickupOK = findViewById(R.id.btnPickupOK);
                        btnPickupOK.setOnClickListener(this);// noch Funktion hinterlegen

                        // Packing Options  Auswahl noch hinzufügen
                        break;
                    case 3:
                        setContentView(R.layout.delivery_info);
                        etAddress = findViewById(R.id.etAddress);
                        etPhone = findViewById(R.id.eTPhone);
                        btnDeliveryOK = findViewById(R.id.btnDeliveryOK);
                        btnDeliveryOK.setOnClickListener(this); // noch Funktion hinterlegen
                        getDeliveryInfo();

                        break;
                    default:

                }
                break;
            case R.id.btnDeliveryOK:
                // Intent für neue activity_send schreiben
                break;
        }
    }
private void getDeliveryInfo(){
        delivaddress = etAddress.getText().toString();
        delivphone = etPhone.getText().toString();
}
    public void readFile(){

        int counter = 0;
        int fileNr  = 0;
        switch (bla){
            case 1:
                inputStream = getResources().openRawResource(R.raw.pizza);
                break;
            case 2:
                inputStream = getResources().openRawResource(R.raw.toppings);
                break;
            case 3:
                inputStream = getResources().openRawResource(R.raw.sauce);
                break;

            default:
                return;
        }
        inputStream = getResources().openRawResource(R.raw.pizza);
        reader      = new BufferedReader(new InputStreamReader(inputStream));

        try {

            while((lineReader = reader.readLine()) != null) {

                output = lineReader.split(";");


                pizzaList[counter] = output[0];

                counter++;
                }


        }

        catch (Exception e ){
            Log.e("Reading failed",e.toString());

        }
        readFile();
    }

    private void setPickupTime(){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog picker;

        picker = new TimePickerDialog(this, this, hour, minute, true);
        picker.setTitle(R.string.alertPickupTimeTitle);
                //.setIcon noch einfügen
        picker.show();
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourofDay, int minute) {
        etPickedTime.setText(String.format("%02d:%02d", hourofDay, minute));
    }
    private AlertDialog selectSize(){
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertSizeTitle)
               // .setIcon()        noch einfügen

        .setSingleChoiceItems(R.array.arraySizes, -1, this );


        alertSize = diabuilder.create();
        alertSize.show();

        return alertSize;
    }

    private AlertDialog takeAway(){
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertTakeAwayTitle);
                // .setIcon()        noch einfügen

                //Spinner schreiben


        alertTake = diabuilder.create();
        alertTake.show();

        return alertSize;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(dialog == alertSize){


        switch(which){

        }
        }
        else if (dialog == alertTake){
            switch (which){

            }
            }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
