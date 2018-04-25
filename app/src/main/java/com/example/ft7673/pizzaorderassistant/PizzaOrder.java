package com.example.ft7673.pizzaorderassistant;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class PizzaOrder extends Activity {


    RadioButton rdbtPizzeria;
    RadioButton rdbtTakeaway;
    RadioButton rdbtDelivery;
    Button      btnOrderOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_type);

        rdbtPizzeria = findViewById(R.id.rdbtPizzeria);
        rdbtTakeaway = findViewById(R.id.rdbtTakeaway);
        rdbtDelivery = findViewById(R.id.rdbtDelivery);
        btnOrderOK   = findViewById(R.id.btnOrderOK);


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
   
}
