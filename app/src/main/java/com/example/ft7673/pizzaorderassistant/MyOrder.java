package com.example.ft7673.pizzaorderassistant;


import java.io.Serializable;

public class MyOrder implements Serializable{
    private double total = 0.00;
    private Pizza[] order;

    public MyOrder(Pizza[] order){
        this.order = order;
        for(int i = 0; i < order.length; i++){
            if(order[i] == null){
                break;
            }
            else {
                this.total = this.total + order[i].getPizzaPrice();
            }
        }
    }
    public double getTotal(){
        return total;
    }
    public Pizza[] getOrder(){
        return order;
    }
}
