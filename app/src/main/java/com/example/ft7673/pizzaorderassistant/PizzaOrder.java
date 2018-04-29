package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class PizzaOrder extends Activity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, DialogInterface.OnClickListener,
        AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener,
        DialogInterface.OnMultiChoiceClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

    private View                orderView;

    private RadioGroup                  rdGroup;
    private RadioButton                 rdbtPizzeria;
    private RadioButton                 rdbtTakeaway;
    private RadioButton                 rdbtDelivery;
    private Button                      btnOrderOK;
    private Button                      btnPickupOK;

    private Button                      btnCheckout;
    private Button                      btnAdd;
    private Button                      btnDeliveryOK;
    private CheckBox                    cbTable;

    private Spinner                     spPizza;
    private Spinner                     spDough;
    private Spinner                     spSize;
    private Spinner                     spSauce;
    private Spinner                     spPckOpt;
    private Switch                      topSw;

    private ArrayAdapter<String>        adprPizza;
    private ArrayAdapter<String>        adprDough;
    private ArrayAdapter<String>        adprSize;
    private ArrayAdapter<String>        adprSauce;
    private ArrayAdapter<String>        adprPckOpt;

    private int                         orderType = 0;                                              // 1= Pizzeria, 2=Takeaway, 3=Delivery
    private int                         cntPiz = 0;
    private int                         csvFile;
    private Double                      priceHelperPz;
    private AlertDialog                 alertSize;
    private AlertDialog                 alertTopping;
    private AlertDialog                 alertTable;

    private InputStream                 inputStream;
    private String[]                    output;
    private String                      lineReader;
    private String                      delivaddress;
    private String                      delivphone;
    private BufferedReader              reader;

    private String[]                    pizzaList;
    private String[]                    sauceList;
    private String[]                    toppingList;
    private String[]                    pzSizeList;
    private String[]                    doughList;

    private Double[]                    pizzaPrice;
    private Double[]                    toppingPrice;

    private Calendar                    calendar = Calendar.getInstance();

    private EditText                    etPacking;
    private EditText                    etAddress;
    private EditText                    etPhone;
    private EditText                    tableNr;

    private boolean[]                   selected;

    private Pizza[]                     porder;
    Intent intent;
    MyOrder                             myOrder;
    TextView                            txtPickedTime;
    int                                 selectedTable;
    NumberPicker                        numberPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_type);


        rdGroup         =  findViewById(R.id.rdGroup);
        rdbtPizzeria    =  findViewById(R.id.rdbtPizzeria);
        rdbtTakeaway    =  findViewById(R.id.rdbtTakeaway);
        rdbtDelivery    =  findViewById(R.id.rdbtDelivery);

        btnOrderOK      =  findViewById(R.id.btnOrderTypeOK);
        btnOrderOK.setOnClickListener(this);

        rdGroup.setOnCheckedChangeListener(this);
        csvFile         = 0;

        priceHelperPz   = 0.00;

        porder          = new Pizza[25];

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
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
        switch (group.getCheckedRadioButtonId()) {
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
                break;
        }
    }

    public void defineOrder() {
        setContentView(R.layout.activity_main);

        orderView   = findViewById(R.id.orderView);

        spPizza     = findViewById(R.id.spPizza);
        spDough     = findViewById(R.id.spDough);
        spSize      = findViewById(R.id.spSize);
        spSauce     = findViewById(R.id.spSauce);
        topSw       = findViewById(R.id.topSw);

        btnAdd      = findViewById(R.id.btnAdd);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbTable     = findViewById(R.id.cbTable);

        btnAdd.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);

        if (orderType == 1) {
            cbTable.setVisibility(View.VISIBLE);
            cbTable.setOnClickListener(this);
        } else {
            cbTable.setVisibility(View.GONE);
        }


        for (int i = 0; i < 3; i++) {
            getCsvSize();
            readFile();
        }
        selected    = new boolean[pizzaList.length];
        adprPizza   = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, pizzaList);
        adprPizza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPizza.setAdapter(adprPizza);
        spPizza.setOnItemSelectedListener(this);


        adprSauce   = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, sauceList);
        adprSauce.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSauce.setAdapter(adprSauce);
        spSauce.setOnItemSelectedListener(this);


        pzSizeList  = getResources().getStringArray(R.array.arraySizes);
        adprSize    = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, pzSizeList);
        adprSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSize.setAdapter(adprSize);
        spSize.setOnItemSelectedListener(this);

        doughList   = getResources().getStringArray(R.array.arrayDough);

        adprDough   = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, doughList);
        adprDough.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDough.setAdapter(adprDough);
        spDough.setOnItemSelectedListener(this);

        topSw.setOnCheckedChangeListener(this);


    }
private AlertDialog selectTable(){
        numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);// 20 tables in the pizzeria

        tableNr = new EditText(this);
        tableNr.setInputType(InputType.TYPE_CLASS_NUMBER);
        tableNr.setRawInputType(Configuration.KEYBOARD_12KEY);
        AlertDialog.Builder diabuiler = new AlertDialog.Builder(this);
        diabuiler.setTitle(R.string.alertTableTitle)
                .setView(tableNr)
                .setPositiveButton(R.string.btnOK, this);
        // setICon
        diabuiler.create();
        alertTable = diabuiler.show();
        return alertTable;


}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOrderTypeOK:
                if(rdGroup.getCheckedRadioButtonId() ==  rdbtPizzeria.getId()){
                    selectTable();
                }
                if (rdGroup.getCheckedRadioButtonId() != -1) {
                    defineOrder();
                }
                else {
                    Toast.makeText(this, R.string.toastSelectType, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnAdd:
                if (checkPizzaInfo()){
                    porder[cntPiz].setPizzaPrice(priceHelperPz);
                    checkSauce();
                    checkSize();
                    cntPiz++;
                    defineOrder();
                }
                break;

            case R.id.btnCheckout:                                                                   // nur erlauben wenn auch was ausgwählt ist
                if(checkPizzaInfo()){
                    porder[cntPiz].setPizzaPrice(priceHelperPz);
                    checkSauce();
                    checkSize();


                switch (orderType) {
                    case 1:                                                                         // in Pizzeria
                        myOrder = new MyOrder(porder);
                        intent = new Intent(this, SentOrder.class);
                        intent.putExtra("order", myOrder);
                        intent.putExtra("ordertype", orderType);
                        //intent.putExtra("tableNR", tableNR);                                      // noch als Extra implementierbar
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, R.string.resultConf);
                        }
                        break;
                    case 2:
                        setContentView(R.layout.takeaway_info);
                        txtPickedTime       = findViewById(R.id.txtPickedTime);
                        btnPickupOK         = findViewById(R.id.btnPickupOK);
                        spPckOpt            = findViewById(R.id.spPckOpt);

                        txtPickedTime.setOnLongClickListener(this);
                        btnPickupOK.setOnClickListener(this);                                       // Packing Options  Auswahl noch hinzufügen

                        String[] arrayPckOpt = getResources().getStringArray(R.array.arrayPckOpt);
                        adprPckOpt   = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, arrayPckOpt);
                        adprPckOpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spPckOpt.setAdapter(adprPckOpt);
                        spPckOpt.setOnItemSelectedListener(this);
                        break;
                    case 3:
                        setContentView(R.layout.delivery_info);
                        etAddress = findViewById(R.id.etAddress);
                        etPhone = findViewById(R.id.eTPhone);
                        btnDeliveryOK = findViewById(R.id.btnDeliveryOK);
                        btnDeliveryOK.setOnClickListener(this);
                        break;
                    default:
                        break;
                }

            }
            else{
                    break;
                }
                break;
            case R.id.btnPickupOK:
                if(txtPickedTime.getText().toString().equals(getResources()
                        .getString(R.string.hintClickTime))){
                    Toast.makeText(this, R.string.toastSelectTime, Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(spPckOpt.getSelectedItem().toString().equals(getResources()
                        .getString(R.string.stringNotSelected))){
                    Toast.makeText(this, R.string.toastSelectPckOpt, Toast.LENGTH_SHORT).show();
                }
                else {
                    MyOrder myOrder = new MyOrder(porder);
                    intent = new Intent(this, SentOrder.class);
                    intent.putExtra("order", myOrder);
                    intent.putExtra("ordertype", orderType);
                    intent.putExtra("packing", "Packing: "
                            + spPckOpt.getSelectedItem().toString());
                    intent.putExtra("time", "Pickup Time: "
                            + txtPickedTime.getText().toString());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, R.string.resultConf);
                    }
                }
                break;
            case R.id.btnDeliveryOK:
                myOrder = new MyOrder(porder);
                intent = new Intent(this, SentOrder.class);
                intent.putExtra("order", myOrder);
                intent.putExtra("ordertype", orderType);
                intent.putExtra("address", "Delivery Address: " + etAddress.getText().toString());
                intent.putExtra("phone", "Phonenumber: " + etPhone.getText().toString());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, R.string.resultConf);
                }
                break;
        }
    }

    private void checkSize(){
        if(spSize.getSelectedItem().toString().equals("Medium")){
            porder[cntPiz].setPizzaPrice(2.00);
    }
    else if(spSize.getSelectedItem().toString().equals("Big")){
            porder[cntPiz].setPizzaPrice(5.00);
        }
        }

    private void checkSauce(){
        if(cbTable.isChecked()){
            //Logik einfügen.
        }
        else if(!spSauce.getSelectedItem().toString().equals("none")){
            porder[cntPiz].setPizzaPrice(2.00);
        }

    }

    private boolean checkPizzaInfo(){
        if (porder[cntPiz].getPizzaName().equals(R.string.stringNotSelected)){
            Toast.makeText(this, R.string.toastNoName, Toast.LENGTH_LONG).show();
            return false;
        }else if(porder[cntPiz].getPizzaDough().equals(getResources().getString(R.string.stringNotSelected))){
            Toast.makeText(this, R.string.toastNoDough, Toast.LENGTH_LONG).show();
            return false;
        }else if(porder[cntPiz].getPizzaSize().equals(getResources().getString(R.string.stringNotSelected))){
            Toast.makeText(this, R.string.toastNoSize, Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            porder[cntPiz].setPizzaToppings(fillTops());
        }

        return true;
    }

    private String[] fillTops() {
        int counter     = 0;
        double topPrice = 0.00;
        for (int i = 0; i < toppingList.length; i++) {
            if (selected[i] == true) {
                topPrice += toppingPrice[i];
                counter++;
            }
        }
        String[] help = new String[counter];
        counter = 0;
        for (int i = 0; i < toppingList.length; i++){
            if (selected[i] == true) {
                help[counter] = toppingList[i];
                counter++;
            }
            else{

            }
        }
        if(counter == 0){
            help    = new String[1];
            help[0] = "none";
        }
        else{

        }
        porder[cntPiz].setPizzaPrice(topPrice);
        return help;
    }

    public void readFile() {

        int counter = 0;
        try {
            switch (csvFile) {
                case 0:
                    inputStream = getResources().openRawResource(R.raw.pizza);
                    reader      = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {

                        output = lineReader.split(";");


                        pizzaList[counter]  = output[0];
                        pizzaPrice[counter] = Double.parseDouble(output[1]);


                        counter++;

                    }
                    csvFile++;
                    break;
                case 1:
                    inputStream = getResources().openRawResource(R.raw.sauce);
                    reader      = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {

                        output = lineReader.split(";");


                        sauceList[counter] = output[0];
                        counter++;


                    }
                    csvFile++;
                    break;
                case 2:
                    inputStream = getResources().openRawResource(R.raw.toppings);
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {

                        output = lineReader.split(";");


                        toppingList[counter]    = output[0];
                        toppingPrice[counter]   = Double.parseDouble(output[1]);
                        counter++;
                    }
                    csvFile++;
                default:
                    return;
            }


        } catch (Exception e) {
            Log.e("Reading failed", e.toString());

        }
    }


    public void getCsvSize() {

        int counter = 0;
        try {
            switch (csvFile) {
                case 0:
                    inputStream     = getResources().openRawResource(R.raw.pizza);
                    reader          = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {
                        counter++;
                    }
                    pizzaList       = new String[counter];
                    pizzaPrice      = new Double[counter];
                    break;
                case 1:
                    inputStream = getResources().openRawResource(R.raw.sauce);
                    reader      = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {
                        counter++;
                    }
                    sauceList = new String[counter];
                    break;
                case 2:
                    inputStream     = getResources().openRawResource(R.raw.toppings);
                    reader          = new BufferedReader(new InputStreamReader(inputStream));

                    while ((lineReader = reader.readLine()) != null) {
                        counter++;
                    }
                    toppingList     = new String[counter];
                    toppingPrice    = new Double[counter];
                default:
                    return;
            }
        } catch (Exception e) {
            Log.e("Reading failed", e.toString());

        }


    }

    private void setPickupTime() {
        int hour    = calendar.get(Calendar.HOUR_OF_DAY);
        int minute  = calendar.get(Calendar.MINUTE);
        TimePickerDialog picker;

        picker = new TimePickerDialog(this, this, hour, minute, true);
        picker.setTitle(R.string.alertPickupTimeTitle);
        //.setIcon noch einfügen
        picker.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourofDay, int minute) {
        txtPickedTime.setText(String.format("%02d:%02d", hourofDay, minute));
    }

    private AlertDialog selectSize() {
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertSizeTitle)
                // .setIcon()        noch einfügen

                .setSingleChoiceItems(R.array.arraySizes, -1, this);


        alertSize = diabuilder.create();
        alertSize.show();

        return alertSize;
    }


    private AlertDialog toppings() {
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder.setTitle(R.string.alertTopTitle);
        // .setIcon()        noch einfügen

        diabuilder.setPositiveButton(R.string.btnOK, this);
        diabuilder.setNegativeButton(R.string.btnCancel, this);
        diabuilder.setMultiChoiceItems(toppingList, selected, this);

        alertTopping = diabuilder.create();
        alertTopping.show();

        return alertTopping;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog == alertSize) {


            switch (which) {

            }
        } else if(dialog == alertTable){
            if(which == DialogInterface.BUTTON_POSITIVE){                               // braucht man das?
                numberPicker.getValue();
            }
        } else if (dialog == alertTopping) {
            boolean help = false;
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    for (int i = 0; i < toppingList.length; i++) {
                        if (selected[i] == true) {
                            help = true;
                        } else {
                            continue;
                        }
                    }
                    if (help) {
                        topSw.setChecked(true);
                    } else {
                        topSw.setChecked(false);
                    }
                    break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        resetTop();
                        break;
            }
        }
        }

    public void resetTop(){                                                                             // resets the topping list when cancel is hit
        for (int i = 0; i < toppingList.length ; i++){
            selected[i] = false;
        }
}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {              // handles the spinners

        if (adapterView == spPizza) {
            Pizza help = new Pizza(pizzaList[i]);
            porder[cntPiz] = help;                                                                  // jedes mal wenn de pizza geändert wird, wird alles andere wieder null gesetzt.
            priceHelperPz = pizzaPrice[i];

            if(spPizza.getSelectedItem().toString().equals(getResources()
                    .getString(R.string.stringNotSelected))){
                orderView.setVisibility(View.INVISIBLE);

            }
            else {
                orderView.setVisibility(View.VISIBLE);

            }
           //porder[cntPiz].setPizzaPrice(Double.parseDouble(pizzaList[i+1]));
        }
        if(adapterView == spDough){
        porder[cntPiz].setPizzaDough(doughList[i]);
        }
        if(adapterView == spSauce){
            if(cbTable.isChecked()){
                porder[cntPiz].setTableSauce(true);
            }
            porder[cntPiz].setPizzaSauce(sauceList[i]);


        }
        if(adapterView == spSize){
            porder[cntPiz].setPizzaSize(pzSizeList[i]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == R.string.resultConf){
            onCreate(null);
            Toast.makeText(this, R.string.resultConf, Toast.LENGTH_LONG).show();
        }
       // super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {                                     // Spinner function
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {                        // Is called when toppings are selected.
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {                        // is called when the switch is pressed  and calls the topping layout
            toppings();
    }

    @Override
    public boolean onLongClick(View v) {                                                            // Long Click Time Picker
        switch (v.getId()){
            case R.id.txtPickedTime:
                setPickupTime();
                return true;

        }
        return false;
    }
}


