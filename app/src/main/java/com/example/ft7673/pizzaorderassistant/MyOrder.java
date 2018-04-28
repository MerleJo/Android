package com.example.ft7673.pizzaorderassistant;


import java.io.Serializable;

public class MyOrder implements Serializable{
    private double total;
    private Pizza[] order;

    public MyOrder(Pizza[] order){
        this.order = order;
        for(int i = 0; i < order.length; i++){
           // this.total = this.total + Pizza[i].getPizzaPrice();         // wo ist der Fehler?
        }
    }
    public double getTotal(){
        return total;
    }
    public Pizza[] getOrder(){
        return order;
    }
}
