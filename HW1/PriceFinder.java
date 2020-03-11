package edu.utep.cs.cs4330.mypricewatcher;

import java.text.DecimalFormat;

public class PriceFinder {
    private double randomDouble;

    public double getNewPrice(String URL){
        System.out.println(URL);
        randomDouble = Math.random() * 2000 + 1;

        return randomDouble;
    }
}

