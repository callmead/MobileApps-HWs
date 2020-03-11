package edu.utep.cs.cs4330.mypricewatcher2;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public PriceFinder myPriceFinder;
    public double oldPrice, newPrice;
    public ArrayList<Item> itemArrayList;
    private ListView myListView;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    public int lastPosition = -1;
    private int itemId;
    private String url;
    public boolean connectionStatus = false;
    DatabaseHelper myDBhelper;
    SimpleCursorAdapter simpleCA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check internet connection and open settings menu...
        if(!networkConnected(MainActivity.this)){
            AlertDialog.Builder bd = new AlertDialog.Builder(MainActivity.this);
            bd.setTitle("Internet Connectivity");
            bd.setMessage("Active internet connection is required to work on this app and your phone is not connected. " +
                    "\n\nEnable Wifi?");
            //adding options to the button
            bd.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface di, int i){
                    Intent x = new Intent(Intent.ACTION_MAIN, null);
                    x.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName c = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                    x.setComponent(c);
                    x.setFlags(x.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(x);
                }
            });
            bd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            bd.create().show();
        }//End Check internet connection and open settings menu...

        //load existing items if any
        myDBhelper = new DatabaseHelper(this);
        myListView = (ListView) findViewById(R.id.myListView);
        loadItemData();

        //context menu
        registerForContextMenu(myListView); //inflate context menu using onCreateContextMenu and the onContextItemSelected
        //On list click
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastPosition = position;
                //prepare data and call DataEntry class

                itemId = Integer.parseInt(((TextView)view.findViewById(R.id.lblItemID1)).getText().toString());
                String name = ((TextView)view.findViewById(R.id.lblItem1)).getText().toString();
                url = ((TextView)view.findViewById(R.id.lblURL1)).getText().toString();
                double price = Double.parseDouble(((TextView)view.findViewById(R.id.lblCP1)).getText().toString());
                String change = ((TextView)view.findViewById(R.id.lblPC1)).getText().toString();

                //Toast.makeText(getApplicationContext(), "Item position="+lastPosition+" ItemID="+itemId, Toast.LENGTH_SHORT).show();
                transferListItem(itemId, name, url, price, change);
            }
        });

        //Long click on the listview
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lastPosition = position;//capture item position in long click also.
                itemId = Integer.parseInt(((TextView)view.findViewById(R.id.lblItemID1)).getText().toString());
                url = ((TextView)view.findViewById(R.id.lblURL1)).getText().toString();
                //Toast.makeText(getApplicationContext(), "Item position="+lastPosition+" ItemID="+itemId, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void loadItemData() {
        try {
            Cursor cursor = myDBhelper.getAllProducts();
            if (cursor == null)
            {
                Toast.makeText(this, "Something went wrong while loading item data!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cursor.getCount() == 0)
            {
                Toast.makeText(this, "No items found in the database", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] columns = new String[] {
                    myDBhelper.KEY_ID,
                    myDBhelper.KEY_ITEM,
                    myDBhelper.KEY_URL,
                    myDBhelper.KEY_PRICE,
                    myDBhelper.KEY_CHANGE
            };
            int[] boundTo = new int[] {
                    R.id.lblItemID1,
                    R.id.lblItem1,
                    R.id.lblURL1,
                    R.id.lblCP1,
                    R.id.lblPC1
            };
            simpleCA = new SimpleCursorAdapter(this, R.layout.adapter_view, cursor, columns, boundTo, 0);
            myListView.setAdapter(simpleCA);
        }catch(Exception e){
            Toast.makeText(this, "Something went wrong while loading item data!\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getNewItemPrices(){
        //not enough time to implement this
        Toast.makeText(this, "This function is not available.", Toast.LENGTH_SHORT).show();
    }

    public void transferListItem(int id, String name, String url, double price, String change){
        Intent i = new Intent(MainActivity.this, DataEntry.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//keeping track of activity stack

        Bundle bundle = new Bundle();
        bundle.putString("Operation", "EditItem");
        bundle.putInt("ID", id);
        bundle.putString("Item", name);
        bundle.putString("URL", url);
        bundle.putDouble("CP", price);
        bundle.putInt("position", lastPosition);
        i.putExtras(bundle);

        startActivity(i);
    }

    //Context Menu...
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

            case R.id.viewWeb:
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//keeping track of activity stack
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
                        if (myDBhelper.deleteProduct(itemId)){
                            //Toast.makeText(getApplicationContext(), "Item position="+lastPosition+" ItemID="+itemId +" deleted", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Something went wrong!\nItem at position="+lastPosition+" with ItemID="+itemId+" not deleted.", Toast.LENGTH_SHORT).show();
                        }
                        loadItemData();
                    }
                });
                bd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//do nothing.
                    }
                });
                bd.create().show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    //Options menu code
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.my_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.mnuNewItem){
            Intent i = new Intent(MainActivity.this, DataEntry.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//keeping track of activity stack
            Bundle bundle = new Bundle();
            bundle.putString("Operation", "AddNewItem");
            i.putExtras(bundle);
            startActivity(i);
        }
        if(id==R.id.mnuFetch){
            getNewItemPrices();
        }
        return super.onOptionsItemSelected(item);
    }

    //checking network status...
    public boolean networkConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
        NetworkInfo n = cm.getActiveNetworkInfo();

        if(n!=null && n.isConnectedOrConnecting()){
            android.net.NetworkInfo WiFi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo Mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //Either wifi or mobile network, one of them should be available.
            if(WiFi!=null && WiFi.isConnectedOrConnecting() || (Mobile!=null && Mobile.isConnectedOrConnecting())){
                connectionStatus = true;
                Toast.makeText(getApplicationContext(), "App connected to the internet", Toast.LENGTH_SHORT).show();
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}