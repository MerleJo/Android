package com.example.ft7673.pizzaorderassistant;


public class Pizza {

    private String name;
    private String dough;
    private String size;
    private String[] toppings;
    private String sauce;
    private double price;

    public Pizza(String name, String dough, String size, String[] toppings, String sauce, double price){
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
    public String getPizzaDough(){
        return this.dough;
    }
    public String getPizzaSize(){
        return this.size;
    }
    public String[] getPizzaToppings(){
        return this.toppings;
    }
    public String getPizzaSauce(){
        return this.sauce;
    }
    public double getPizzaPrice(){
        return this.price;
    }
    public double setPizzaPrice(double price){
        this.price = this.price + price;
    }
}
