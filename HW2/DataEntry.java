package edu.utep.cs.cs4330.mypricewatcher2;

import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataEntry extends AppCompatActivity {
    //EditText txtItem, txtURL, txtPrice, txtCP, txtPC;
    EditText txtItem, txtURL, txtCP;
    Button btnAdd, btnEdit, btnRefresh, btnBrowse;
    MainActivity ma;
    PriceFinder myPriceFinder;
    private double itemCurrentPrice;
    private String priceChangeUpdate;
    private int receivedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ma = new MainActivity();
        myPriceFinder = new PriceFinder();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtItem = findViewById(R.id.txtItem);
        txtURL = findViewById(R.id.txtURL);/*
        txtURL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//Once URL loses focus, fetch price

                }
            }
        });*/
        //txtPrice = findViewById(R.id.txtPrice);
        txtCP = findViewById(R.id.txtCP);
        //txtPC = findViewById(R.id.txtPC);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this::AddClicked);

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this::EditClicked);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this::refreshClicked);

        btnBrowse = findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(this::browseClicked);

        //Check for passed data between activities...
        Bundle b;
        b = getIntent().getExtras();
        if(b!=null){//Add new Item
            if(b.getString("Operation").equals("AddNewItem")){
                btnEdit.setEnabled(false);
                txtCP.setEnabled(false);
                Toast.makeText(this, "Please enter item details", Toast.LENGTH_SHORT).show();
            }else{//Edit Item
                btnAdd.setEnabled(false);
                btnRefresh.setEnabled(false);
                txtCP.setEnabled(false);
                //Toast.makeText(this, "You can simply view or change item details", Toast.LENGTH_SHORT).show();
                //Load text fields with selected item data from the listview
                txtItem.setText(b.getString("Item"));
                txtURL.setText(b.getString("URL"));
                //txtPrice.setText(b.getString("Price"));
                txtCP.setText(b.getString("CP"));
                //txtPC.setText(b.getString("PC"));
                priceChangeUpdate = b.getString("PC");
                receivedPosition = b.getInt("position");
            }
        }
    }
    public void AddClicked(View view) {
        //Toast.makeText(this, "Adding item!", Toast.LENGTH_SHORT).show();
        if (txtItem.getText().toString().matches("")||txtURL.getText().toString().matches("")||txtCP.getText().toString().matches("")) {
            Toast.makeText(this, "Please provide item details and fetch item price in order to save", Toast.LENGTH_SHORT).show();
            txtItem.requestFocus();
        }else {
            itemOperation("AddNewItem");
        }
    }
    public void EditClicked(View view) {
        //Toast.makeText(this, "Updating item!", Toast.LENGTH_SHORT).show();
        itemOperation("EditItem");
    }
    public void itemOperation(String operation){
        String itemX = txtItem.getText().toString();
        String urlX = txtURL.getText().toString();
        //String priceX = txtPrice.getText().toString();
        String currentPriceX = txtCP.getText().toString();
        String priceChangeX = priceChangeUpdate;

        Intent i = new Intent(DataEntry.this, MainActivity.class);
        i.putExtra("Operation", operation);
        i.putExtra("Item", itemX);
        i.putExtra("URL", urlX);
        //i.putExtra("Price", priceX);
        i.putExtra("CP", currentPriceX);
        i.putExtra("PC", priceChangeX);
        //send selected item position in case of edit item
        if(operation.equals("EditItem")){
            i.putExtra("position", receivedPosition);
        }

        startActivity(i);
        finish();//close this activity.
    }

    public void refreshClicked(View view){
        String u = txtURL.getText().toString();
        if (u.matches("")) {
            Toast.makeText(this, "Please provide item URL", Toast.LENGTH_SHORT).show();
            txtURL.requestFocus();
            return;//if textfield empty return
        }else {
            try {
                itemCurrentPrice = myPriceFinder.getNewPrice(txtURL.getText().toString());
                txtCP.setText((String.format("%.2f", itemCurrentPrice)));
                //priceChangeUpdate = myPriceFinder.calculateChange(ma.newPrice, ma.oldPrice);
                priceChangeUpdate = "Newly added item";
                //txtPC.setText(priceChangeUpdate);

                //txtPC.setText(myPriceFinder.calculateChange(myPriceFinder.getNewPrice(txtURL.getText().toString()), ma.oldPrice));
            }catch(NumberFormatException n){
                n.printStackTrace();
            }
        }
    }

    public void browseClicked(View view){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(txtURL.getText().toString()));
        startActivity(i);
    }
}
