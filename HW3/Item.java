package edu.utep.cs.cs4330.mypricewatcher2;

import java.text.DecimalFormat;

public class Item {
    private int itemID;
    private String itemName;
    private String itemURL;
    private Double itemNewPrice;
    private String itemChange;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public Item(){}//default constructor

    public Item(String itemName, String itemURL, Double itemNewPrice, String itemChange){//overloading
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemNewPrice = itemNewPrice;
        this.itemChange = itemChange;
    }

    public Item(int itemID, String itemName, String itemURL, Double itemNewPrice, String itemChange){//overloading
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemURL = itemURL;
        this.itemNewPrice = itemNewPrice;
        this.itemChange = itemChange;
    }

    public int getItemId(){return this.itemID;}
    public void setItemId(int itemID){this.itemID = itemID;}

    public String getItemName() {
        return this.itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemURL() {
        return this.itemURL;
    }
    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public double getItemNewPrice() {
        return this.itemNewPrice;
    }
    public void setItemNewPrice(Double itemNewPrice) {
        this.itemNewPrice = itemNewPrice;
    }

    public String getItemChange() {
        return this.itemChange;
    }
    public void setItemChange(String itemChange) {
        this.itemChange = itemChange;
    }
}
