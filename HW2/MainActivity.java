package edu.utep.cs.cs4330.mypricewatcher2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public PriceFinder myPriceFinder;
    public double oldPrice, newPrice;
    public ArrayList<Item> itemArrayList;
    private ListView myListView;
    public ItemListAdapter myAdapter;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    public int lastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load items to the list
        loadMyData();
        //Check for passed data between activities...
        Bundle b = getIntent().getExtras();
        if(b!=null){
            //Toast.makeText(getApplicationContext(), "2nd Time", Toast.LENGTH_SHORT).show();
            boolean BundleHasKey = b.containsKey("Operation");//if key is available in the bundle or not
            if(BundleHasKey) {
                //Toast.makeText(getApplicationContext(), "Found Key", Toast.LENGTH_SHORT).show();
                if (b.getString("Operation").equals("EditItem")) {//check if its editing.
                    lastPosition = b.getInt("position");
                }
                //workOnListItem(b.getString("Operation"), b.getString("Item"), b.getString("URL"), b.getString("Price"), b.getString("CP"), b.getString("PC"));
                workOnListItem(b.getString("Operation"), b.getString("Item"), b.getString("URL"), b.getString("CP"), b.getString("PC"));
            }
        }

        //context menu
        registerForContextMenu(myListView); //inflate context menu using onCreateContextMenu and the onContextItemSelected

        //On list click
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastPosition = position;
                //Toast.makeText(getApplicationContext(), "Item "+position+" clicked", Toast.LENGTH_SHORT).show();
                //call DataEntry class
                transferListItem();
            }
        });

        //Long click on the listview
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lastPosition = position;//capture item position in long click also.
                return false;
            }
        });

    }
    public void loadMyData(){
        myPriceFinder = new PriceFinder();

        myListView = (ListView) findViewById(R.id.myListView);

        //creating new item objects
        //Item item01 = new Item("HP ProBook", "https://www.hp.com","1500", "1450", "Dropped");

        oldPrice = 599.99;
        newPrice = myPriceFinder.getNewPrice("https://goo.gl/UMi4k1");
        //Item item01 = new Item("HP ProBook 450 G3", "https://goo.gl/UMi4k1", Double.toString(oldPrice), Double.toString(newPrice), myPriceFinder.calculateChange(newPrice, oldPrice));
        Item item01 = new Item("HP ProBook 450 G3", "https://goo.gl/UMi4k1", Double.toString(newPrice), myPriceFinder.calculateChange(newPrice, oldPrice));
        oldPrice = 249.68;
        newPrice = myPriceFinder.getNewPrice("https://goo.gl/aZXAb3");
        Item item02 = new Item("DELL I3565", "https://goo.gl/aZXAb3", Double.toString(newPrice), myPriceFinder.calculateChange(newPrice, oldPrice));
        oldPrice = 238.98;
        newPrice = myPriceFinder.getNewPrice("https://goo.gl/hTLZT2");
        Item item03 = new Item("HP Flyer Red", "https://goo.gl/hTLZT2", Double.toString(newPrice), myPriceFinder.calculateChange(newPrice, oldPrice));
        oldPrice = 629.00;
        newPrice = myPriceFinder.getNewPrice("https://goo.gl/ENK5AQ");
        Item item04 = new Item("Lenovo Business", "https://goo.gl/ENK5AQ", Double.toString(newPrice), myPriceFinder.calculateChange(newPrice, oldPrice));

        //Creating array list and adding item objects to it.
        itemArrayList = new ArrayList<>();
        itemArrayList.add(item01);
        itemArrayList.add(item02);
        itemArrayList.add(item03);
        itemArrayList.add(item04);
        itemArrayList.add(item01);
        itemArrayList.add(item02);
        itemArrayList.add(item03);
        itemArrayList.add(item04);

        //Creating custom item list adapter
        myAdapter = new ItemListAdapter(this, R.layout.adapter_view, itemArrayList);
        myListView.setAdapter(myAdapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mInf = getMenuInflater();
        mInf.inflate(R.menu.my_context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo ACM = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent i;
        switch (item.getItemId()){//which item is selected
            case R.id.priceChange:
                //loadMyData();
                //fetch item prices
                //Toast.makeText(getApplicationContext(), "First "+myListView.getFirstVisiblePosition()+" last "+myListView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                for(int j = 0; j<=myListView.getLastVisiblePosition(); j++) {//loop to get current values

                    String itemX = itemArrayList.get(j).getItemName();
                    String urlX = itemArrayList.get(j).getItemURL();
                    String currentPriceX = df2.format(Double.parseDouble(itemArrayList.get(j).getItemNewPrice()));
                    //String priceChangeX = itemArrayList.get(j).getItemChange();

                    oldPrice = Double.parseDouble(itemArrayList.get(j).getItemNewPrice());
                    newPrice = myPriceFinder.getNewPrice(urlX);

                    String priceChangeUpdate = myPriceFinder.calculateChange(newPrice, oldPrice);

                    Item it01 = new Item(itemX, urlX, Double.toString(newPrice), priceChangeUpdate);

                    itemArrayList.set(j, it01);
                    myAdapter.notifyDataSetChanged();

                }

                Toast.makeText(this, "New item prices loaded", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.viewWeb:
                //Toast.makeText(getApplicationContext(), "Item "+lastPosition+" clicked", Toast.LENGTH_SHORT).show();
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(itemArrayList.get(lastPosition).getItemURL()));
                startActivity(i);
                return true;
            case R.id.addItem:
                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this, DataEntry.class);
                i.putExtra("Operation", "AddNewItem");
                startActivity(i);

                return true;
            case R.id.deleteItem:
                //Yes/No custom dialog
                AlertDialog.Builder bd = new AlertDialog.Builder(MainActivity.this);
                bd.setTitle("Confirm");
                bd.setMessage("Are you sure you want to delete the selected item?");
                //adding options to the button
                bd.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface di, int i){
                        itemArrayList.remove(ACM.position);
                        myAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Item Deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
                bd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, "Great", Toast.LENGTH_SHORT).show();
                    }
                });
                bd.create().show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
        //return super.onContextItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //public void workOnListItem(String operationX, String itemX, String urlX, String priceX, String currentPriceX, String priceChangeX){
    public void workOnListItem(String operationX, String itemX, String urlX, String currentPriceX, String priceChangeX){
        //Toast.makeText(this, itemX+ urlX+ priceX+ currentPriceX+ priceChangeX, Toast.LENGTH_SHORT).show();
        //Add new item
        //Item it01 = new Item(itemX, urlX, priceX, currentPriceX, priceChangeX);
        Item it01 = new Item(itemX, urlX, currentPriceX, priceChangeX);
        if(operationX.equals("AddNewItem")) {
            //Toast.makeText(getApplicationContext(), "Item "+lastPosition+" clicked", Toast.LENGTH_SHORT).show();
            itemArrayList.add(it01);
            Toast.makeText(this, "Item Added in the list", Toast.LENGTH_SHORT).show();
        }else{

            //Toast.makeText(getApplicationContext(), "Item "+lastPosition+" clicked", Toast.LENGTH_SHORT).show();
            itemArrayList.set(lastPosition, it01);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
        myAdapter.notifyDataSetChanged();
    }
    public void transferListItem(){
        Intent i = new Intent(MainActivity.this, DataEntry.class);
        i.putExtra("Operation", "EditItem");
        i.putExtra("Item", itemArrayList.get(lastPosition).getItemName());
        i.putExtra("URL", itemArrayList.get(lastPosition).getItemURL());
        //i.putExtra("Price", itemArrayList.get(lastPosition).getItemPrice());
        i.putExtra("CP",df2.format(Double.parseDouble(itemArrayList.get(lastPosition).getItemNewPrice())));
        i.putExtra("PC", itemArrayList.get(lastPosition).getItemChange());
        i.putExtra("position", lastPosition);
        //Toast.makeText(getApplicationContext(), "Item "+lastPosition+" clicked", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
