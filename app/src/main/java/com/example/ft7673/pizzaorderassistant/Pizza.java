package com.example.ft7673.pizzaorderassistant;


public class Pizza {

    private String name;
    private String dough;
    private String size;
    private String[] toppings;
    private String sauce;
    private double price;

    public Pizza(String name){
        this.name = name;
    }
    public Pizza(String name, String dough, String size, String[] toppings, String sauce, double price){        // checken ob wir es brauchen
        this.name = name;
        this.dough = dough;
        this.size = size;
        this.toppings = toppings;
        this.sauce = sauce;
        this.price = price;
    }

    public String getPizzaName(){
        return this.name;
    }
    public void setPizzaName(String name){
        this.name = name;
    }
    public String getPizzaDough(){
        return this.dough;
    }
    public void setPizzaDough(String dough){
        this.dough = dough;
    }
    public String getPizzaSize(){
        return this.size;
    }
    public void setPizzaSize(String size){
        this.size = size;
    }
    public String[] getPizzaToppings(){
        return this.toppings;
    }
    public void setPizzaToppings(String[] toppings){
        this.toppings = toppings;
    }
    public String getPizzaSauce(){
        return this.sauce;
    }
    public void setPizzaSauce(String sauce){
        this.sauce = sauce;
    }
    public double getPizzaPrice(){
        return this.price;
    }
    public void setPizzaPrice(double price){
        this.price = this.price + price;
    }
}
