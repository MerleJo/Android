package com.example.ft7673.pizzaorderassistant;




public class MyOrder {
    private double total;
    private String[] order;

    public MyOrder(double total, String[] order){
        this.total = total;
        this.order = order;
    }
    public double getTotal(){
        return total;
    }
    public String[] getOrder(){
        return order;
    }
}
