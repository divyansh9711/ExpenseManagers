package com.example.android.expensemanager;

import android.support.v4.content.ContextCompat;

public class ExpenseItem {
    private String amount;
    private String category;
    private String description;
    private String date;
    private String time;
    private int colorId;
    private String id;
    public ExpenseItem(){}

    public ExpenseItem(String amount, String description, String date, String time){
        this.amount=amount;


        this.category="Amount Added";
        this.description=description;
        this.date =date;
        this.time =time;
        colorId = R.color.income;
    }

    public ExpenseItem(String amount, String category, String description, String date, String time){
        this.amount=amount;
        this.category=category;
        this.description=description;
        this.date =date;
        this.time =time;
        colorId = R.color.expense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount(){return amount;}
    public String getCategory(){return category;}
    public  String getDescription(){return description;}
    public String getDate(){return date;}
    public String getTime(){return time;}
    public int getColor(){return colorId;}
}
