package com.example.ft7673.pizzaorderassistant;


public class MyOrder {
    private double total;
    private Pizza[] order;

    public MyOrder(Pizza[] order){
        this.order = order;
        for(int i = 0; i < order.length; i++){
           this.total = this.total + order[i].getPizzaPrice();
        }
    }
    public double getTotal(){
        return total;
    }
    public Pizza[] getOrder(){
        return order;
    }
}
