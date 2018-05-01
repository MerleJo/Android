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

    private View                        orderView;                                                  // used to force the user to write first the pizza while making the rest invisible

    private RadioGroup                  rdGroup;                                                    // Is used in the layout order type to select the order type
    private RadioButton                 rdbtPizzeria;

    private Button                      btnOrderOK;                                                 // Various buttons in the layouts to navigate through them
    private Button                      btnPickupOK;
    private Button                      btnCheckout;
    private Button                      btnAdd;
    private Button                      btnDeliveryOK;

    private AlertDialog                 alertTopping;                                               // Used to diffentiate which buttons where called in the onClick of the alerts
    private AlertDialog                 alertTable;

    private EditText                    etAddress;                                                  // These are used to write down customer specific information that are needed later
    private EditText                    etPhone;
    private EditText                    tableNr;

    private CheckBox                    cbTable;                                                    // Checkbox to see if the sauce should be for the whole table

    private TextView                    txtPickedTime;                                              // Displays the chosen time for the takeaway

    private Spinner                     spPizza;                                                    //Spinners and a switch to set the pizza order
    private Spinner                     spDough;
    private Spinner                     spSize;
    private Spinner                     spSauce;
    private Spinner                     spPckOpt;
    private Switch                      topSw;

    private InputStream                 inputStream;                                                // These are used to to read the csv files
    private String[]                    output;
    private String                      lineReader;
    private BufferedReader              reader;

    private ArrayAdapter<String>        adpr;                                                       // Adapter to get the spinners ready

    private int                         orderType;                                                  // 1= Pizzeria, 2=Takeaway, 3=Delivery
    private int                         cntPiz;                                                     // Show many pizzas there are
    private int                         csvFile;                                                    // indicates which csvFile should be read next
    private int                         selectedTable;                                              // Is the number of the table if you eat in the pizzeria

    private double                      grpSaucPrice;
    private double                      priceHelperPz;                                              // used to help write down the prices
    private Double[]                    pizzaPrice;                                                 // saves the prices for every Pizza
    private Double[]                    toppingPrice;                                               // saves the prices of every topping

    private String[]                    pizzaList;                                                  // saves all the items out of the csv's or string arrays
    private String[]                    sauceList;
    private String[]                    toppingList;
    private String[]                    pzSizeList;
    private String[]                    doughList;

    private boolean                     checkedChange;                                              // help variable to handle the on/off function of the switch
    private boolean[]                   selected;                                                   // is needed for the toppings, saves which toppings are selected

    private Calendar                    calendar = Calendar.getInstance();                          // used for the time picker

    private Pizza[]                     porder;                                                     // saves all of our pizzaobject

    private Intent                      intent;

    private MyOrder                     myOrder;                                                    // Is used for calculating the price with all of the pizzas


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

        porder          = new Pizza[25];                                                            // every order can have a maximum of 25 pizzas
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {                               // used the handle the menu
        switch (item.getItemId()) {
            case R.id.mnuExit:
                finish();                                                                           // ends the app and closes it
                break;
            case R.id.mnuCancel:                                                                    // Cancels the order, resets the app and sets it to the beginning.
                Toast.makeText(this, R.string.toastOrderCancel, Toast.LENGTH_SHORT).show();
                onCreate(new Bundle());
                break;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
        return true;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {                                 // used to see which radiobutton is clicked and sets the order type
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


    public void defineOrder() {                                                                     // is used to initiate the second layout were the pizza toppings etc is chosen
        setContentView(R.layout.activity_main);

        orderView   = findViewById(R.id.orderView);                                                 // defines all of the variables and connects them to the layout
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

        if (orderType == 1) {                                                                       // if the pizza is eaten in the pizzeria you can choose that there will be a sauce
            cbTable.setVisibility(View.VISIBLE);                                                    // for the whole group
            cbTable.setOnClickListener(this);
        } else {
            cbTable.setVisibility(View.GONE);
        }
        for (int i = 0; i < 3; i++) {                                                               // calls the function to read all of the csv files is called 3 times to read all of them
            getCsvSize();
            readFile();
        }
        selected    = new boolean[toppingList.length];                                              // sets the sice of the boolean list of the settings to the optimal size

        adpr = new ArrayAdapter<String>(this,                                               // adapters are initialized and used to set the spinners.
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

        topSw.setOnCheckedChangeListener(this);                                                     // The listener for the switch is set
    }


    private void getCsvSize() {                                                                     // sets the optimal size of the arrays to read the csv files so there will bo no null values
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
                    break;
            }
        } catch (Exception e) {
            Log.e(getResources().getString(R.string.errorReading), e.toString());
        }
    }


    private void readFile() {                                                                       // Reads what is written in every csv File
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
                    break;
            }
        } catch (Exception e) {
            Log.e(getResources().getString(R.string.errorReading), e.toString());
        }
    }


    @Override
    public void onClick(View v) {                                                                   // handles all of the buttons
        switch (v.getId()) {
            case R.id.btnOrderTypeOK:                                                               // Button on the first layout
                if(rdGroup.getCheckedRadioButtonId() ==  rdbtPizzeria.getId()){                     // If the order type is in the pizzeria
                    selectTable();
                }
                if (rdGroup.getCheckedRadioButtonId() != -1) {                                      // if any order type is selected
                    defineOrder();
                }
                else {
                    Toast.makeText(this, R.string.toastSelectType,                          // if there is nothing selected you can't progress
                            Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnAdd:                                                                       // The add button on the main layout to add the pizza to the order and make a new one
                if(cntPiz == porder.length-1){                                                      // if there are too many pizzas you are informed to make a new order and checkout
                    Toast.makeText(this, R.string.toastTooMany, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (checkPizzaInfo() && checkSauce()){                                              // if the fields of Pizza, dough, size and sauce (if group sauce) are filledout correctly
                    porder[cntPiz].setPizzaPrice(priceHelperPz);                                    // the price will be calculated and put to the pizza
                    if(cbTable.isChecked()){                                                        // is used to show in a later layout that there is a group pizza
                        porder[cntPiz].setTableSauce(true);
                    }
                    checkSize();                                                                    // checks the size and adds a price if necessary
                    cntPiz++;                                                                       // used for the initialisation of the next pizza object and calls it right after
                    defineOrder();
                }
                break;

            case R.id.btnCheckout:                                                                  // finishes the order and send all created pizzas to the pizza shop while checking
                if(checkPizzaInfo() && checkSauce()){                                               // if everything is okay
                    if(cbTable.isChecked()){
                        porder[cntPiz].setTableSauce(true);
                    }
                    porder[cntPiz].setPizzaPrice(priceHelperPz);
                    checkSize();

                    switch (orderType) {                                                            // depending on the order type different actions will be done
                        case 1:                                                                     // in Pizzeria
                            myOrder = new MyOrder(porder, grpSaucPrice);                            // intent to send the order to the pizzashop
                            intent = new Intent(this, SentOrder.class);
                            intent.putExtra(getResources().getString(R.string.intentMesTable), selectedTable);
                            intent.putExtra(getResources().getString(R.string.intentMesOrder), myOrder);
                            intent.putExtra(getResources().getString(R.string.intentMesOrderType), orderType);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, R.string.resultConf);
                            }
                            break;
                        case 2:                                                                     // Takeaway
                            setContentView(R.layout.takeaway_info);                                 // Information to get the pizza will be written in the called layout
                            txtPickedTime       = findViewById(R.id.txtPickedTime);
                            btnPickupOK         = findViewById(R.id.btnPickupOK);
                            spPckOpt            = findViewById(R.id.spPckOpt);

                            txtPickedTime.setOnLongClickListener(this);
                            btnPickupOK.setOnClickListener(this);

                            String[] arrayPckOpt = getResources()                                   // sets the packing options in a spinner
                                    .getStringArray(R.array.arrayPckOpt);
                            adpr   = new ArrayAdapter<String>(this,
                                    android.R.layout.simple_spinner_item, arrayPckOpt);
                            adpr.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);

                            spPckOpt.setAdapter(adpr);
                            spPckOpt.setOnItemSelectedListener(this);
                            break;
                        case 3:                                                                     // Delivery
                            setContentView(R.layout.delivery_info);                                 // for the delivery a layout is called where you type the adress an a phone number in
                            etAddress = findViewById(R.id.etAddress);
                            etPhone = findViewById(R.id.eTPhone);
                            btnDeliveryOK = findViewById(R.id.btnDeliveryOK);
                            btnDeliveryOK.setOnClickListener(this);
                            break;
                        default:
                            break;
                    }
                }
                break;

            case R.id.btnPickupOK:                                                                  // used on the takeaway layout. Checks if the fields are correct
                if(txtPickedTime.getText().toString().equals(getResources()
                        .getString(R.string.hintClickTime))){
                    Toast.makeText(this, R.string.toastSelectTime
                            , Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(spPckOpt.getSelectedItem().toString().equals(getResources()
                        .getString(R.string.stringNotSelected))){
                    Toast.makeText(this, R.string.toastSelectPckOpt
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    MyOrder myOrder = new MyOrder(porder);                                          // after everything is checked the order will be given to a new activity
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

            case R.id.btnDeliveryOK:                                                                // The button for the deliverylayout
                if(etAddress.getText().toString().equals("")){                                      // checks if the fields are empty and if not procedess
                    Toast.makeText(this, R.string.toastSelectAdress
                            , Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(etPhone.getText().toString().equals("")){
                    Toast.makeText(this, R.string.toastSelectPhNr, Toast.LENGTH_SHORT).show();
                }
                else {
                    myOrder = new MyOrder(porder);                                                  // after everything is checked the order will be given to a new activity
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
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {                                        // On click for the Alerts dialogs. Checks which alert dialog is used
        if(dialog == alertTable){
            if(which == DialogInterface.BUTTON_POSITIVE){                                           // Checks if a table is selected
                if (tableNr.getText().toString().equals("")){
                    Toast.makeText(this, R.string.toastSelectTable,
                            Toast.LENGTH_SHORT).show();
                    selectTable();
                } else {
                    selectedTable = Integer.parseInt(tableNr.getText().toString());
                }
            }
        } else if (dialog == alertTopping) {                                                        // This is used to handle the toppings. and sets the switch according to if sth. is clicked
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

                    } else {
                        topSw.setChecked(false);
                    }
                    checkedChange = false;
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
    public boolean onLongClick(View v) {                                                            // Long click for the time picker
        switch (v.getId()){
            case R.id.txtPickedTime:
                setPickupTime();
                return true;
        }
        return false;
    }


    private void checkSize(){                                                                       // checks if the pizza is medium or big and sets then an extra price
        if(spSize.getSelectedItem().toString().equals(getResources().getString(R.string.stringMedium))){
            porder[cntPiz].setPizzaPrice(Double.parseDouble(getResources().
                    getString(R.string.mediumPizPrice)));
        }else if(spSize.getSelectedItem().toString().equals(getResources().getString(R.string.stringBig))){
            porder[cntPiz].setPizzaPrice(Double.parseDouble(getResources().
                    getString(R.string.largePizPrice)));
        }
    }


    private boolean checkSauce(){                                                                   // checks if there is a group sauce and if its non and calculates the sauce price if sth
        if(cbTable.isChecked()){                                                                    // is selected
            if(spSauce.getSelectedItem().toString().equals(getResources().getString(R.string.stringNone))){
                Toast.makeText(this, R.string.toastSauceNot, Toast.LENGTH_LONG).show();
                return false;
            }else{
                grpSaucPrice += Double.parseDouble(getResources()
                        .getString(R.string.grpSaucePrice));
                return true;
            }
        }else if(!spSauce.getSelectedItem().toString().equals(getResources().getString(R.string.stringNone))){
            porder[cntPiz].setPizzaPrice(Double.parseDouble(getResources()
                    .getString(R.string.saucePrice)));
            return true;
        }else{
            return true;
        }
    }


    private boolean checkPizzaInfo(){                                                               // checks if dough size and pizza is selected
       if(porder[cntPiz].getPizzaSize().equals(getResources()
               .getString(R.string.stringNotSelected))){
            Toast.makeText(this, R.string.toastNoSize, Toast.LENGTH_LONG).show();
            return false;
        }else if(porder[cntPiz].getPizzaDough().equals(getResources()
               .getString(R.string.stringNotSelected))){
            Toast.makeText(this, R.string.toastNoDough, Toast.LENGTH_LONG).show();
            return false;
        }else{
            porder[cntPiz].setPizzaToppings(fillTops());
        }
        return true;
    }


    private String[] fillTops() {                                                                   // counts the required toppings and puts the price in the pizza object
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


    private void resetTop(){                                                                         // resets the topping list if the cancel button is pressed
        for (int i = 0; i < toppingList.length ; i++){
            selected[i] = false;
        }
    }


    private void setPickupTime() {                                                                  // is used to initiate the timepicker
        int minute     =  calendar.get(Calendar.MINUTE) + 30;
        int hour    = calendar.get(Calendar.HOUR_OF_DAY);

        if(minute >= 60){
            minute -= 60;
            hour ++;
            if(hour >= 24){
                hour-=24;
            }
        }
        TimePickerDialog picker;

        picker = new TimePickerDialog(this, this, hour, minute, true);
        picker.setTitle(R.string.alertPickupTimeTitle);
        picker.setIcon(R.drawable.time);
        picker.show();
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourofDay, int minute) {                       // is used to control that the time is at least half an hour in the future
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


    private AlertDialog selectTable(){                                                              // the alert dialog to choose the table
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


    private AlertDialog toppings() {                                                                // alert dialog to choose the toppings
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
            porder[cntPiz] = help;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                 // is called when 2. Activity OK button is pressed to return to this Activity and reset app for a new order
        if(resultCode == R.string.resultConf){
            onCreate(null);
            Toast.makeText(this, R.string.resultConf, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {                                     // Spinner if nothing is selected
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