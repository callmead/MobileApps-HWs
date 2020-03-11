package edu.utep.cs.cs4330.mypricewatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private PriceFinder myPriceFinder;

    private TextView lblItem;
    private TextView lblPrice;
    private TextView lblCurrentPrice;
    private TextView lblChange;
    private Button btnRefresh;

    private String itemName="HP ProBook 450 G2";
    private int initialPrice=1500;
    private int newPrice=0;
    private int changeInPrice=0;
    private String finalChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPriceFinder = new PriceFinder();

        lblItem = findViewById(R.id.lblItem);
        lblItem.setText("Item: "+itemName);
        lblPrice = findViewById(R.id.lblPrice);
        lblPrice.setText("Initial Price: "+initialPrice);
        lblCurrentPrice = findViewById(R.id.lblCurrentPrice);
        lblCurrentPrice.setText("Current Price: ...");
        lblChange = findViewById(R.id.lblChange);
        lblChange.setText("Price Change: ...");

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this::refreshClicked);
    }
    public void refreshClicked(View view) {
        Toast.makeText(this, "Fetching new data!", Toast.LENGTH_SHORT).show();
        newPrice = (int)myPriceFinder.getNewPrice("www.google.com");

        lblCurrentPrice.setText("Current Price: "+newPrice);

        if (newPrice>initialPrice){
            finalChange = "Price increased $"+(newPrice - initialPrice);
        }else{
            finalChange = "Price dropped $"+(initialPrice - newPrice);
        }
        lblChange.setText(finalChange);
    }
}
