package edu.utep.cs.cs4330.mypricewatcher2;

import java.text.DecimalFormat;

public class Item {
    private String itemName;
    private String itemURL;
    //private String itemPrice;
    private String itemNewPrice;
    private String itemChange;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    //public Item(String itemName, String itemURL, String itemPrice, String itemNewPrice, String itemChange){
    public Item(String itemName, String itemURL, String itemNewPrice, String itemChange){
        this.itemName = itemName;
        this.itemURL = itemURL;
        //this.itemPrice = itemPrice;
        this.itemNewPrice = itemNewPrice;
        this.itemChange = itemChange;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemURL() {
        return itemURL;
    }
    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }
    /*
    public String getItemPrice() {return itemPrice;}
    public void setItemPrice(String itemPrice) {this.itemPrice = itemPrice;}
    */
    public String getItemNewPrice() {
        return itemNewPrice;
    }
    public void setItemNewPrice(String itemNewPrice) {
        this.itemNewPrice = itemNewPrice;
    }

    public String getItemChange() {
        return itemChange;
    }
    public void setItemChange(String itemChange) {
        this.itemChange = itemChange;
    }
}
