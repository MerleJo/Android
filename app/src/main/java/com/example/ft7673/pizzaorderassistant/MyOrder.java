package com.example.ft7673.pizzaorderassistant;

import java.io.Serializable;

public class MyOrder implements Serializable{

    private double  total;
    private Pizza[] order;
    private String  tableSauceList;
    private double  tableSaucePrice;

    public MyOrder(Pizza[] order, double tableSaucePrice){                                          // The order object is created out of the Pizzaarrays. This will be called if the meal is eaten in the pizzeria
        this.order              = order;
        this.tableSauceList     = "none";
        this.tableSaucePrice    = tableSaucePrice;
        this.total              = tableSaucePrice;

        for(int i = 0; i < order.length; i++){                                                      // checks every pizza in the order and sums the price up
            if(order[i] == null){
                break;
            }else {
                this.total = this.total + order[i].getPizzaPrice();
            }
        }
    }


    public MyOrder(Pizza[] order){                                                                  // Constructor for takeaway and delivery since there is no sauce for the group option
        this.order              = order;
        this.tableSauceList     = "none";
        this.tableSaucePrice    = 0.00;

        for(int i = 0; i < order.length; i++){                                                      // checks every pizza in the order and sums the price up
            if(order[i] == null){
                break;
            }else {
                this.total = this.total + order[i].getPizzaPrice();
            }
        }
    }


    public double getTotal(){
        return total;
    }
    public double getTableSaucePrice(){
        return tableSaucePrice;
    }

    public String[] writeOrder(){                                                                   // writes the pizzas in the listview
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


    public String[] getMoreOrder(){                                                                 // writes the extra information into the alertDialog
        String[]    toSave     = new String[order.length];
        String      saveTop;
        String      help;

        for(int i = 0; i < order.length; i++){
            saveTop = "";
            if(order[i] == null){
                break;
            }else{
                for(int j = 0; j < order[i].getPizzaToppings().length; j++){
                    if (j+1 != order[i].getPizzaToppings().length){
                        saveTop = saveTop + order[i].getPizzaToppings()[j] + ", ";
                    }else{
                        saveTop = saveTop + order[i].getPizzaToppings()[j];
                    }
                }
                help = String.format("%.2f", order[i].getPizzaPrice());
                toSave[i] = "Dough: " + order[i].getPizzaDough() + "\n" +
                            "Toppings: " + saveTop + "\n" +
                            "Sauce: " + order[i].getPizzaSauce() + "\n" +
                            "Price: " + help + " €";
            }
        }
        return toSave;
    }


    public String findTableSauce(){                                                                 // Checks if there is a table sauce and sets it if yes. It also deletes it out of the pizza order then
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
