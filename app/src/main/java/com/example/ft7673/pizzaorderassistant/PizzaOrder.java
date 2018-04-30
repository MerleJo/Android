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

    private Button                      btnOrderOK;
    private Button                      btnPickupOK;
    private Button                      btnCheckout;
    private Button                      btnAdd;
    private Button                      btnDeliveryOK;

    private AlertDialog                 alertTopping;
    private AlertDialog                 alertTable;

    private EditText                    etAddress;
    private EditText                    etPhone;
    private EditText                    tableNr;

    private CheckBox                    cbTable;

    private TextView                    txtPickedTime;

    private Spinner                     spPizza;
    private Spinner                     spDough;
    private Spinner                     spSize;
    private Spinner                     spSauce;
    private Spinner                     spPckOpt;
    private Switch                      topSw;

    private InputStream                 inputStream;
    private String[]                    output;
    private String                      lineReader;
    private BufferedReader              reader;

    private ArrayAdapter<String>        adpr;

    private int                         orderType;                                              // 1= Pizzeria, 2=Takeaway, 3=Delivery
    private int                         cntPiz;
    private int                         csvFile;
    private int                         selectedTable;

    private double                      grpSaucPrice;
    private double                      priceHelperPz;
    private Double[]                    pizzaPrice;
    private Double[]                    toppingPrice;

    private String[]                    pizzaList;
    private String[]                    sauceList;
    private String[]                    toppingList;
    private String[]                    pzSizeList;
    private String[]                    doughList;

    private boolean                     checkedChange;
    private boolean[]                   selected;

    private Calendar                    calendar = Calendar.getInstance();

    private Pizza[]                     porder;

    private Intent                      intent;

    private MyOrder                     myOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_type);


        rdGroup         =  findViewById(R.id.rdGroup);
        rdbtPizzeria    =  findViewById(R.id.rdbtPizzeria);
        btnOrderOK      =  findViewById(R.id.btnOrderTypeOK);

        btnOrderOK.setOnClickListener(this);
        rdGroup.setOnCheckedChangeListener(this);

        orderType       = 0;
        cntPiz          = 0;
        csvFile         = 0;
        priceHelperPz   = 0.00;
        grpSaucPrice    = 0.00;

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
            case R.id.mnuExit:
                finish();
                break;
            case R.id.mnuCancel:
                Toast.makeText(this, R.string.toastOrderCancel, Toast.LENGTH_SHORT).show();
                onCreate(new Bundle());
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

        checkedChange = false;

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
        adpr = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pizzaList);
        adpr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPizza.setAdapter(adpr);
        spPizza.setOnItemSelectedListener(this);


        adpr = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sauceList);
        adpr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSauce.setAdapter(adpr);
        spSauce.setOnItemSelectedListener(this);


        pzSizeList  = getResources().getStringArray(R.array.arraySizes);
        adpr    = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pzSizeList);
        adpr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSize.setAdapter(adpr);
        spSize.setOnItemSelectedListener(this);


        doughList   = getResources().getStringArray(R.array.arrayDough);
        adpr   = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, doughList);
        adpr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDough.setAdapter(adpr);
        spDough.setOnItemSelectedListener(this);

        topSw.setOnCheckedChangeListener(this);
    }


    private void readFile() {
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
            Log.e(getResources().getString(R.string.errorReading), e.toString());
        }
    }


    private void getCsvSize() {
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
            Log.e(getResources().getString(R.string.errorReading), e.toString());
        }
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
                if(cntPiz == porder.length-1){
                    Toast.makeText(this, R.string.toastTooMany, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (checkPizzaInfo()== true && checkSauce() == true){
                    porder[cntPiz].setPizzaPrice(priceHelperPz);
                    if(cbTable.isChecked()){
                        porder[cntPiz].setTableSauce(true);
                    }
                    checkSize();
                    cntPiz++;
                    defineOrder();
                }
                break;

            case R.id.btnCheckout:
                if(checkPizzaInfo() == true && checkSauce() == true){
                    if(cbTable.isChecked()){
                        porder[cntPiz].setTableSauce(true);
                    }
                    porder[cntPiz].setPizzaPrice(priceHelperPz);
                    checkSize();

                    switch (orderType) {
                        case 1:                                                                     // in Pizzeria
                            myOrder = new MyOrder(porder, grpSaucPrice);
                            intent = new Intent(this, SentOrder.class);
                            intent.putExtra(getResources().getString(R.string.intentMesTable), selectedTable);
                            intent.putExtra(getResources().getString(R.string.intentMesOrder), myOrder);
                            intent.putExtra(getResources().getString(R.string.intentMesOrderType), orderType);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, R.string.resultConf);
                            }
                            break;
                        case 2:                                                                     // Takeaway
                            setContentView(R.layout.takeaway_info);
                            txtPickedTime       = findViewById(R.id.txtPickedTime);
                            btnPickupOK         = findViewById(R.id.btnPickupOK);
                            spPckOpt            = findViewById(R.id.spPckOpt);

                            txtPickedTime.setOnLongClickListener(this);
                            btnPickupOK.setOnClickListener(this);

                            String[] arrayPckOpt = getResources().getStringArray(R.array.arrayPckOpt);
                            adpr   = new ArrayAdapter<String>(this,
                                    android.R.layout.simple_spinner_item, arrayPckOpt);
                            adpr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spPckOpt.setAdapter(adpr);
                            spPckOpt.setOnItemSelectedListener(this);
                            break;
                        case 3:                                                                     // Delivery
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
                    intent.putExtra(getResources().getString(R.string.intentMesOrder), myOrder);
                    intent.putExtra(getResources().getString(R.string.intentMesOrderType), orderType);
                    intent.putExtra(getResources().getString(R.string.intentMesPacking),
                            getResources().getString(R.string.stringPartPacking) + " "
                            + spPckOpt.getSelectedItem().toString());
                    intent.putExtra(getResources().getString(R.string.intentMesTime),
                            getResources().getString(R.string.stringPartTime) + " "
                            + txtPickedTime.getText().toString());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, R.string.resultConf);
                    }
                }
                break;
            case R.id.btnDeliveryOK:
                if(etAddress.getText().toString().equals("")){
                    Toast.makeText(this, R.string.toastSelectAdress, Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(etPhone.getText().toString().equals("")){
                    Toast.makeText(this, R.string.toastSelectPhNr, Toast.LENGTH_SHORT).show();
                }
                else {
                    myOrder = new MyOrder(porder);
                    intent = new Intent(this, SentOrder.class);
                    intent.putExtra(getResources().getString(R.string.intentMesOrder), myOrder);
                    intent.putExtra(getResources().getString(R.string.intentMesOrderType), orderType);
                    intent.putExtra(getResources().getString(R.string.intentMesAddress),
                            getResources().getString(R.string.stringPartDelivery)+
                                    " " + etAddress.getText().toString());
                    intent.putExtra(getResources().getString(R.string.intentMesPhone),
                            getResources().getString(R.string.stringPartPhone)+
                                    " " + etPhone.getText().toString());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, R.string.resultConf);
                    }
                }
                break;
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(dialog == alertTable){
            if(which == DialogInterface.BUTTON_POSITIVE){
                if (tableNr.getText().toString().equals("")){
                    Toast.makeText(this, R.string.toastSelectTable,
                            Toast.LENGTH_SHORT).show();
                    selectTable();
                } else {
                    selectedTable = Integer.parseInt(tableNr.getText().toString());
                }
            }
        } else if (dialog == alertTopping) {
            checkedChange = true;
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
                        checkedChange = false;

                    } else {
                        topSw.setChecked(false);
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    resetTop();
                    topSw.setChecked(false);
                    checkedChange = false;
                    break;
            }
        }
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


    private void checkSize(){
        if(spSize.getSelectedItem().toString().equals(getResources().getString(R.string.stringMedium))){
            porder[cntPiz].setPizzaPrice(2.00);
        }else if(spSize.getSelectedItem().toString().equals(getResources().getString(R.string.stringBig))){
            porder[cntPiz].setPizzaPrice(5.00);
        }
    }


    private boolean checkSauce(){
        if(cbTable.isChecked()){
            if(spSauce.getSelectedItem().toString().equals(getResources().getString(R.string.stringNone))){
                Toast.makeText(this, R.string.toastSauceNot, Toast.LENGTH_LONG).show();
                return false;
            }else{
                grpSaucPrice += 6.00;
                return true;
            }
        }else if(!spSauce.getSelectedItem().toString().equals(getResources().getString(R.string.stringNone))){
            porder[cntPiz].setPizzaPrice(2.00);
            return true;
        }else{
            return true;
        }
    }


    private boolean checkPizzaInfo(){
       if(porder[cntPiz].getPizzaSize().equals(getResources().getString(R.string.stringNotSelected))){
            Toast.makeText(this, R.string.toastNoSize, Toast.LENGTH_LONG).show();
            return false;
        }else if(porder[cntPiz].getPizzaDough().equals(getResources().getString(R.string.stringNotSelected))){
            Toast.makeText(this, R.string.toastNoDough, Toast.LENGTH_LONG).show();
            return false;
        }else{
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
        String[] help   = new String[counter];
        counter         = 0;
        for (int i = 0; i < toppingList.length; i++){
            if (selected[i] == true) {
                help[counter] = toppingList[i];
                counter++;
            }
        }
        if(counter == 0){
            help    = new String[1];
            help[0] = getResources().getString(R.string.stringNone);
        }
        porder[cntPiz].setPizzaPrice(topPrice);
        return help;
    }


    private void resetTop(){                                                                         // resets the topping list when cancel is hit
        for (int i = 0; i < toppingList.length ; i++){
            selected[i] = false;
        }
    }


    private void setPickupTime() {
        int hour    = calendar.get(Calendar.HOUR_OF_DAY);
        int minute  = calendar.get(Calendar.MINUTE);
        TimePickerDialog picker;

        picker = new TimePickerDialog(this, this, hour, minute, true);
        picker.setTitle(R.string.alertPickupTimeTitle);
        picker.setIcon(R.drawable.time);
        picker.show();
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourofDay, int minute) {
        int helpMin     =  calendar.get(Calendar.MINUTE) + 30;
        int helpHour    = calendar.get(Calendar.HOUR_OF_DAY);

        if(helpMin >= 60){
            helpMin -= 60;
            helpHour ++;
            if(helpHour >= 24){
                helpHour-=24;
            }
        }
        if(  hourofDay > helpHour
                || (hourofDay == helpHour
                && minute > helpMin)){
            txtPickedTime.setText(String.format("%02d:%02d", hourofDay, minute));
        }else{
            Toast.makeText(this,R.string.toastTimeNot, Toast.LENGTH_LONG).show();
        }
    }


    private AlertDialog selectTable(){
        tableNr = new EditText(this);
        tableNr.setInputType(InputType.TYPE_CLASS_NUMBER);
        tableNr.setRawInputType(Configuration.KEYBOARD_12KEY);

        AlertDialog.Builder diabuiler = new AlertDialog.Builder(this);
        diabuiler   .setTitle(R.string.alertTableTitle)
                    .setView(tableNr)
                    .setPositiveButton(R.string.btnOK, this)
                    .setIcon(R.drawable.table_nr);

        diabuiler.create();
        alertTable = diabuiler.show();
        return alertTable;
    }


    private AlertDialog toppings() {
        AlertDialog.Builder diabuilder = new AlertDialog.Builder(this);
        diabuilder  .setTitle(R.string.alertTopTitle)
                    .setIcon(R.drawable.toppings)
                    .setPositiveButton(R.string.btnOK, this)
                    .setNegativeButton(R.string.btnCancel, this)
                    .setMultiChoiceItems(toppingList, selected, this);

        alertTopping = diabuilder.create();
        alertTopping.show();

        return alertTopping;
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

            }else {
                orderView.setVisibility(View.VISIBLE);
            }
        }
        if(adapterView == spDough){
            porder[cntPiz].setPizzaDough(doughList[i]);
        }
        if(adapterView == spSauce){
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {                                     // Spinner function
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {                        // Is called when toppings are selected.
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {                        // is called when the switch is pressed  and calls the topping layout
        if(checkedChange == false) {
            toppings();
        } else{
            checkedChange = false;
        }

    }
}