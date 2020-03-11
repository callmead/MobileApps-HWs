package edu.utep.cs.cs4330.mypricewatcher2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "itemsDB";
    private static final String TABLE_NAME = "items";

    public static final String KEY_ID = "_id";
    public static final String KEY_ITEM = "item";
    public static final String KEY_URL = "url";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CHANGE = "change";

    public DatabaseHelper(Context context){
        super (context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ITEM + " TEXT, "
                + KEY_URL + " TEXT, "
                + KEY_PRICE + " REAL, "
                + KEY_CHANGE + " TEXT" + ")";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_ITEM, KEY_URL, KEY_PRICE, KEY_CHANGE}, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }else{
            return null;
        }
    }

    public void addProduct(Item item) {
        ContentValues values = new ContentValues();

        values.put(KEY_ITEM, item.getItemName());
        values.put(KEY_URL, item.getItemURL());
        values.put(KEY_PRICE, item.getItemNewPrice());
        values.put(KEY_CHANGE, item.getItemChange());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean updateProduct(Item i) {
        boolean result = false;
        String q = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + i.getItemId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(q, null);
        if (c.moveToFirst())
        {
            String q2 = "UPDATE " + TABLE_NAME + " SET "
                    + KEY_ITEM + " = \"" + i.getItemName() + "\", "
                    + KEY_URL + " = \"" + i.getItemURL() + "\", "
                    + KEY_PRICE + " = " +  i.getItemNewPrice() + ", "
                    + KEY_CHANGE + " = \"" + i.getItemChange() + "\" "
                    + " WHERE " + KEY_ID + " = " + i.getItemId();
            db.execSQL(q2);
            result = true;
        }
        db.close();
        return result;
    }

    public boolean deleteProduct(int id) {
        boolean result = false;
        String q = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        Item i = new Item();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            i.setItemId(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(i.getItemId())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void deleteAllProducts(){
        String q = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(q);
        db.close();
    }
}
