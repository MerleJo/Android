package com.example.ft7673.pizzaorderassistant;


import java.io.Serializable;

public class MyOrder implements Serializable{
    private double total = 0.00;
    private Pizza[] order;
    private String tableSauceList;

    public MyOrder(Pizza[] order){
        this.order = order;
        this.tableSauceList = "none";
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
    public String[] writeOrder(){
        String[] toSave = new String[order.length];
        for(int i = 0; i < order.length; i++){
            if(order[i] == null){
                break;
            }else{
                 toSave[i] = order[i].getPizzaName() + "(" + order[i].getPizzaSize() + ")";
            }
        }
        return toSave;
    }
    public String[] getMoreOrder(){
        String[] toSave = new String[order.length];
        for(int i = 0; i < order.length; i++){
            if(order[i] == null){
                break;
            }else{
                String saveTop = "";
                for(int j = 0; j < order[i].getPizzaToppings().length; j++){
                    if (j+1 != order[i].getPizzaToppings().length){
                        saveTop = saveTop + order[i].getPizzaToppings()[j] + ", ";
                    }else{
                        saveTop = saveTop + order[i].getPizzaToppings()[j];
                    }

                }
                toSave[i] = "Dough: " + order[i].getPizzaDough() + "\n" +
                            "Toppings: " + saveTop + "\n" +
                            "Sauce: " + order[i].getPizzaSauce();
            }
        }
        return toSave;
    }
    public String findTableSauce(){
        for (int i = 0; i < order.length; i++){
            if(order[i] == null){
                break;
            }else if (order[i].getTableSauce() == true){
                if(tableSauceList.equals("none")){
                    tableSauceList = order[i].getPizzaSauce();
                }else{
                    tableSauceList = tableSauceList + ", " +order[i].getPizzaSauce();
                }
                order[i].setPizzaSauce("none");
            }
        }
        return tableSauceList;
    }
}
