package com.example.ft7673.pizzaorderassistant;

import java.io.Serializable;

public class Pizza implements Serializable{

    private String      name;
    private String      dough;
    private String      size;
    private String      sauce;
    private String[]    toppings;
    private boolean     tableSauce;
    private double      price;


    public Pizza(String name){                                                                      //The pizza object will be created with just the name and the other values will be set to a default values
        this.name       = name;
        this.dough      = "not selected";
        this.size       = "not selected";
        this.sauce      = "none";
        tableSauce      = false;
    }


    public String getPizzaName(){                                                                   // getters and setters for the pizza class
        return this.name;
    }
    public String   getPizzaDough(){
        return this.dough;
    }
    public String   getPizzaSize(){return this.size;}
    public String[] getPizzaToppings(){return this.toppings;}
    public String   getPizzaSauce(){return this.sauce;}
    public double   getPizzaPrice(){return this.price;}
    public boolean  getTableSauce(){return this.tableSauce;}

    public void setPizzaDough(String dough){this.dough = dough;}
    public void setPizzaSize(String size){
        this.size = size;
    }
    public void setPizzaToppings(String[] toppings){this.toppings = toppings;}
    public void setPizzaSauce(String sauce){
        this.sauce = sauce;
    }
    public void setPizzaPrice(double price){
        this.price = this.price + price;
    }
    public void setTableSauce(boolean tableSauce){ this.tableSauce = tableSauce;}
}