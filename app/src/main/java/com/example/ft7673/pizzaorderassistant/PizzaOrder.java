package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class PizzaOrder extends Activity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, DialogInterface.OnClickListener,
        AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener,
        DialogInterface.OnMultiChoiceClickListener{


    RadioGroup                      rdGroup;
    RadioButton                     rdbtPizzeria;
    RadioButton                     rdbtTakeaway;
    RadioButton                     rdbtDelivery;
    Button                          btnOrderOK;
    Button                          btnPickupOK;

    Button                          btnCheckout;
    Button                          btnAdd;
    Button                          btnDeliveryOK;
    CheckBox                        cbTable;

    private Spinner                 spPizza;
    private Spinner                 spDough;
    private Spinner                 spSize;
    private Spinner                 spSauce;

    private ArrayAdapter<String>    adprPizza;
    private ArrayAdapter<String>    adprDough;
    private ArrayAdapter<String>    adprSize;
    private ArrayAdapter<String>    adprSauce;

    private int                     orderType = 0;                                                      // 1= Pizzeria, 2=Takeaway, 3=Delivery
    private int                     cntPiz = 0;
    private int                     csvFile;
    AlertDialog                     alertSize;
    AlertDialog                     alertTake;

    private InputStream             inputStream;
    private String[]                output;
    private String                  lineReader;
    private String                  delivaddress;
    private String                  delivphone;
    private BufferedReader          reader;

    private String[]                pizzaList;
    private String[]                sauceList;
    private String[]                toppingList;
    private String[]                pzSize;

    private Calendar                calendar;

    private EditText                etPickedTime;
    private EditText                etPacking;
    private EditText                etAddress;
    private EditText                etPhone;

    private boolean[]               selected;

    private String[][]              order;
    private Pizza[]                 porder;
    Intent                          intent;


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
        csvFile = 0;

        order       = new String[20][50];


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
        spSauce     = findViewById(R.id.spSauce);
        //spPizza.setPromptId(R.string.hintPizza); need a hint

        btnAdd      = findViewById(R.id.btnAdd);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbTable     = findViewById(R.id.cbTable);

        btnAdd.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        if(orderType == 1){
            cbTable.setVisibility(View.VISIBLE);
            cbTable.setOnClickListener(this);
        }else{
            cbTable.setVisibility(View.INVISIBLE);
        }


        for(int i = 0 ; i<3 ; i++) {
            getCsvSize();
            readFile();
        }
        selected    = new boolean[pizzaList.length];
        adprPizza = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pizzaList);
        adprPizza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPizza.setAdapter(adprPizza);
        spPizza.setOnItemSelectedListener(this);

        adprSauce = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sauceList);
        adprSauce.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSauce.setAdapter(adprSauce);


        pzSize = getResources().getStringArray(R.array.arraySizes);
        adprSize = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pzSize);
        adprSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSize.setAdapter(adprSize);

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
                defineOrder();
                break;

            case R.id.btnCheckout:
                switch (orderType){
                    case 1:                                                                          // in Pizzeria
                        intent = new Intent(this, SentOrder.class );
                        intent.putExtra("order", porder);
                        intent.putExtra("ordertype", orderType);
                        //intent.putExtra("tableNR", tableNR);                                      // noch als Extra implementierbar
                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        setContentView(R.layout.takeaway_info);
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
            case R.id.btnPickupOK:
                intent = new Intent(this, SentOrder.class );
                intent.putExtra("order", porder);
                intent.putExtra("ordertype", orderType);
                intent.putExtra("packing", etPacking.getText().toString());
                intent.putExtra("time", etPickedTime.getText().toString());
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.btnDeliveryOK:
                intent = new Intent(this, SentOrder.class );
                intent.putExtra("order", porder);
                intent.putExtra("ordertype", orderType);
                intent.putExtra("address", etAddress.getText().toString());
                intent.putExtra("phone", etPhone.getText().toString());
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
        }
    }
private void getDeliveryInfo(){
        delivaddress = etAddress.getText().toString();
        delivphone = etPhone.getText().toString();
}
    public void readFile() {

        int counter = 0;
        try {
            switch (csvFile) {
                case 0:
                    inputStream = getResources().openRawResource(R.raw.pizza);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {

                        output = lineReader.split(";");


                        //pizzaList[counter] = output[0];
                        porder[counter].setPizzaName(output[0]);
                        porder[counter].setPizzaPrice(Double.parseDouble(output[1]));                   // ins CSV File noch den Preis reinschreiben

                        //counter++;

                    }
                    csvFile++;
                    break;
                case 1:
                    inputStream = getResources().openRawResource(R.raw.sauce);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while((lineReader = reader.readLine()) != null) {

                        output = lineReader.split(";");


                        //sauceList[counter] = output[0];
                        //counter++;

                        porder[counter].setPizzaSauce(output[0]);
                        porder[counter].setPizzaPrice(Double.parseDouble(output[1]));

                    }
                    csvFile++;
                    break;
                case 2:
                    inputStream = getResources().openRawResource(R.raw.toppings);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while((lineReader = reader.readLine()) != null) {

                        output = lineReader.split(";");


                       // toppingList[counter] = output[0];
                       // counter++;

                        //porder[counter].setPizzaToppings(output[0]);                               String Array oder String?
                       // porder[counter].setPizzaPrice();                                              wie Preis berechnen

                    }
                    csvFile++;
                default:
                    return;
            }


            } catch (Exception e) {
                Log.e("Reading failed", e.toString());

            }
        }


    public void getCsvSize(){

        int counter = 0;
        try {
            switch (csvFile) {
                case 0:
                    inputStream = getResources().openRawResource(R.raw.pizza);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {
                        counter++;
                    }


                    pizzaList = new String[counter];
                    break;
                case 1:
                    inputStream = getResources().openRawResource(R.raw.sauce);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {
                        counter++;
                    }

                    sauceList = new String[counter];
                    break;
                case 2:
                    inputStream = getResources().openRawResource(R.raw.toppings);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {
                        counter++;
                    }


                    toppingList = new String[counter];
                default:
                    return;
            }
        }
        catch (Exception e ){
            Log.e("Reading failed",e.toString());

        }


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

    private AlertDialog toppings(){
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertTopTitle);
        // .setIcon()        noch einfügen

        diabuilder.setPositiveButton(R.string.btnOK, this);
        diabuilder.setNegativeButton(R.string.btnCancel, this);
        diabuilder.setMultiChoiceItems(pizzaList, selected, this);


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

        Pizza help = new Pizza(pizzaList[i]);
        porder[cntPiz] = help;
      ////  order[0][cntPiz] = pizzaList[i];                //speichert die Pizza beim anklicken im Spinner in das speicherarray
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        if(b == true){

        }
    }
}
