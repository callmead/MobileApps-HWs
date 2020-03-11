package edu.utep.cs.cs4330.mypricewatcher2;

import java.text.DecimalFormat;

public class PriceFinder {
    private double randomDouble;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    //public double getNewPrice(String URL){
    public double getNewPrice(String urlOfItem){
        System.out.println(urlOfItem);
        randomDouble = Math.random() * 1000 + 1;
        return randomDouble;
    }
    public String calculateChange(double np, double op){
        String outPut;
        double p;
        if (np < op){
            p = (op - np)/op*100;
            outPut = "Price dropped "+df2.format(p)+"%";
        }else{
            p = (np - op)/op*100;
            outPut = "Price increased "+df2.format(p)+"%";
        }
        return outPut;
    }
}

