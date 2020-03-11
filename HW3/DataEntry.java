package edu.utep.cs.cs4330.mypricewatcher2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class DataEntry extends AppCompatActivity {
    EditText txtItem, txtURL, txtCP;
    private int itemID, receivedPosition;
    Button btnAdd, btnEdit, btnRefresh, btnBrowse;
    //MainActivity ma;
    PriceFinder myPriceFinder;
    public double itemCurrentPrice, currentPriceX, oldPrice, newPrice;
    public String priceChangeUpdate, itemX, urlX, priceChangeX, url;
    DatabaseHelper myDBhelper;
    SimpleCursorAdapter simpleCA;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ma = new MainActivity();
        myPriceFinder = new PriceFinder();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtItem = (EditText)findViewById(R.id.txtItem);
        txtURL = (EditText)findViewById(R.id.txtURL);
        txtCP = (EditText)findViewById(R.id.txtCP);

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
                btnBrowse.setEnabled(false);
                Toast.makeText(this, "Please enter item name, URL and fetch price", Toast.LENGTH_SHORT).show();
            }
            if(b.getString("Operation").equals("EditItem")){
                btnAdd.setEnabled(false);
                //btnRefresh.setEnabled(false);
                txtCP.setEnabled(false);

                //Load text fields with selected item data from the listview
                itemID = b.getInt("ID");
                Double cp = b.getDouble("CP");

                txtItem.setText(b.getString("Item"));
                txtURL.setText(b.getString("URL"));
                txtCP.setText(cp.toString());
                priceChangeUpdate = b.getString("PC");
                receivedPosition = b.getInt("position");

                oldPrice = cp;
            }
        }
        //Database
        myDBhelper = new DatabaseHelper(this);

        txtURL.setOnFocusChangeListener(new View.OnFocusChangeListener() {//fill in wwww and http://
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//Once URL loses focus, fetch price
                    checkFields();
                }
            }
        });
    }

    public void AddClicked(View view) {
        if(checkFields()){
            if(txtCP.getText().toString().equals("")){
                fetchPrice();
            }
            try {
                Item i = new Item(itemX, urlX, currentPriceX, "Newly Added item");

                myDBhelper.addProduct(i);
                Toast.makeText(this, "New item added in database", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Toast.makeText(this, "Something went wrong while adding the new item!\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            goBacktoMainActivity();
        }
    }

    public void EditClicked(View view) {
        if(checkFields()){
            priceChangeUpdate = myPriceFinder.calculateChange(newPrice, oldPrice);
            try {
                Item i = new Item(itemID, itemX, urlX, currentPriceX, priceChangeUpdate);

                if (myDBhelper.updateProduct(i)) {
                    Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Item record not updated!", Toast.LENGTH_SHORT).show();
                    txtItem.requestFocus();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong while updating item record!\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            goBacktoMainActivity();
        }
    }
    //Fetch the item price.
    public void refreshClicked(View view){
        if(checkFields()){
            fetchPrice();
        }
    }

    public void browseClicked(View view){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(txtURL.getText().toString()));
        startActivity(i);
    }

    //Method to verify field data
    public boolean checkFields(){
        if (txtItem.getText().toString().matches("")||txtURL.getText().toString().matches("")) {
            Toast.makeText(this, "Please provide item details", Toast.LENGTH_SHORT).show();
            txtItem.requestFocus();
            return false;
        }else {
            url = txtURL.getText().toString();
            if(!url.startsWith("www.")&& !url.startsWith("http")){
                url = "www."+url;
            }
            if(!url.startsWith("http")){
                url = "http://"+url;
            }
            txtURL.setText(url);

            itemX = txtItem.getText().toString();
            urlX = url;
            if(!txtCP.getText().toString().equals("")){
                currentPriceX = Double.parseDouble(txtCP.getText().toString());
            }
            priceChangeX = priceChangeUpdate;
            //fetchPrice();
            return true;
        }
    }
    public void fetchPrice(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    itemCurrentPrice = myPriceFinder.getNewPrice(txtURL.getText().toString());
                }catch(IOException e){e.printStackTrace();}
                setPrice();
            }
        };
        new Thread(r).start();
    }
    public void setPrice(){
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                txtCP.setText((String.format("%.2f", itemCurrentPrice)));
                priceChangeUpdate = "Newly added item";
                currentPriceX = Double.parseDouble(txtCP.getText().toString());
                newPrice = currentPriceX;
                if(currentPriceX==0) {//if no price found, show a message
                    Toast ifx = Toast.makeText(getApplicationContext(), "The provided URL does not have any recognized pattern product information!", Toast.LENGTH_SHORT);
                    ifx.show();
                }else{
                    Toast ifx = Toast.makeText(getApplicationContext(), "Pattern matched!\nFound item price: "+newPrice, Toast.LENGTH_SHORT);
                    ifx.show();
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    //After Add or Edit item, calling back the main activity.
    public void goBacktoMainActivity(){
        //Passing the intent to Main Activity!
        Intent j = new Intent(DataEntry.this, MainActivity.class);
        startActivity(j);
        DataEntry.this.finish();
    }
}
