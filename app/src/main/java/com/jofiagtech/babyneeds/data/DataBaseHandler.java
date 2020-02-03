package com.jofiagtech.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.jofiagtech.babyneeds.model.Item;
import com.jofiagtech.babyneeds.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DataBaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABY_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_BABY_ITEM + " INTEGER,"
                + Util.KEY_COLOR + " TEXT,"
                + Util.KEY_QTY_NUMBER + " INTEGER,"
                + Util.KEY_ITEM_SIZE + " INTEGER,"
                + Util.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_BABY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);

        onCreate(db);
    }

    // CRUD operations
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_BABY_ITEM, item.getItemName());
        values.put(Util.KEY_COLOR, item.getItemColor());
        values.put(Util.KEY_QTY_NUMBER, item.getItemQuantity());
        values.put(Util.KEY_ITEM_SIZE, item.getItemSize());
        values.put(Util.KEY_DATE_NAME, System.currentTimeMillis());//timestamp of the system

        //Inset the row
        db.insert(Util.TABLE_NAME, null, values);

        Log.d("DBHandler", "added Item: ");
    }

    //Get an Item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_BABY_ITEM,
                        Util.KEY_COLOR,
                        Util.KEY_QTY_NUMBER,
                        Util.KEY_ITEM_SIZE,
                        Util.KEY_DATE_NAME},
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();
        if (cursor != null) {
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_BABY_ITEM)));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Util.KEY_COLOR)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_QTY_NUMBER)));
            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_ITEM_SIZE)));

            //convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE_NAME)))
                    .getTime()); // Feb 23, 2020

            item.setDateItemAdded(formattedDate);


        }

        return item;
    }

    //Get all Items
    public List<Item> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_BABY_ITEM,
                        Util.KEY_QTY_NUMBER,
                        Util.KEY_COLOR,
                        Util.KEY_ITEM_SIZE,
                        Util.KEY_DATE_NAME},
                null, null, null, null,
                Util.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(cursor.getInt(0));
                item.setItemName(cursor.getString(1));
                item.setItemQuantity(cursor.getInt(2));
                item.setItemColor(cursor.getString(3));
                item.setItemSize(cursor.getInt(4));
                /*item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_BABY_ITEM)));
                item.setItemColor(cursor.getString(cursor.getColumnIndex(Util.KEY_COLOR)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_QTY_NUMBER)));
                item.setItemSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_ITEM_SIZE)));*/

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE_NAME)))
                        .getTime()); // Feb 23, 2020
                item.setDateItemAdded(formattedDate);

                //Add to arraylist
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return itemList;

    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_BABY_ITEM, item.getItemName());
        values.put(Util.KEY_COLOR, item.getItemColor());
        values.put(Util.KEY_QTY_NUMBER, item.getItemQuantity());
        values.put(Util.KEY_ITEM_SIZE, item.getItemSize());
        values.put(Util.KEY_DATE_NAME, System.currentTimeMillis());//timestamp of the system

        //update row
        return db.update(Util.TABLE_NAME, values,
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});

    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME,
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();
    }

    public void deleteAllItem(){
        SQLiteDatabase db = this.getReadableDatabase();

        for (Item item : this.getAllItems())
            db.delete(Util.TABLE_NAME,
                    Util.KEY_ID + "=?",
                    new String[]{String.valueOf(item.getId())});

        db.close();
    }

    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;

    }

}


/*
    public class DataBaseHandler extends SQLiteOpenHelper {

    public DataBaseHandler(Context context ) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    //We create our table
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME
                + "("
                + Util.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + Util.COLUMN_NAME + " TEXT, "
                + Util.COLUMN_QUANTITY + " INTEGER, "
                + Util.COLUMN_COLOR + "TEXT, "
                + Util.COLUMN_SIZE + "INTEGER"
                + ");";
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.drop_table);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        //Create a table again
        onCreate(db);
    }

    // Create (add Item)
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.COLUMN_NAME, item.getName());
        values.put(Util.COLUMN_QUANTITY, item.getQuantity());
        values.put(Util.COLUMN_COLOR, item.getColor());
        values.put(Util.COLUMN_SIZE, item.getSize());

        //Insert to row
        db.insert(Util.TABLE_NAME, null, values);
        //Log.d("DBHandler", "addItem: " + "item added");
        db.close(); //closing db connection!

    }

    //Read 1st part (get a contact)
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.COLUMN_ID,
                        Util.COLUMN_NAME,
                        Util.COLUMN_QUANTITY,
                        Util.COLUMN_COLOR,
                        Util.COLUMN_SIZE},
                Util.COLUMN_ID +"=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);



        Item item = new Item();

        if (cursor != null) {
            cursor.moveToFirst();

            item.setId(Integer.parseInt(String.valueOf(cursor.getInt(0))));
            item.setName(cursor.getString(1));
            item.setQuantity(cursor.getInt(2));
            item.setColor(cursor.getString(3));
            item.setSize(cursor.getInt(4));

            cursor.close();
        }

        return item;
    }

    //Read 2nd part get all Contacts
    public List<Item> getAllItem() {
        List<Item> itemArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        //Select all contacts
        String selectAll = String.format("SELECT * FROM %s", Util.TABLE_NAME);
        Cursor cursor = db.rawQuery(selectAll, null);

        //Loop through our data
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                item.setQuantity(cursor.getInt(2));
                item.setColor(cursor.getString(3));
                item.setSize(cursor.getInt(4));

                //add item objects to our list
                itemArrayList.add(item);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return itemArrayList;
    }

    //Update item
    public int updateItem(Item item){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.COLUMN_NAME, item.getName());
        values.put(Util.COLUMN_QUANTITY, item.getQuantity());
        values.put(Util.COLUMN_COLOR, item.getColor());
        values.put(Util.COLUMN_SIZE, item.getSize());

        return db.update(Util.TABLE_NAME, values, Util.COLUMN_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    //Delete a item
    public void deleteItem(Item item){
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(Util.TABLE_NAME, Util.COLUMN_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    //Delete all item
    public void deleteAllItem(){
        SQLiteDatabase db = this.getReadableDatabase();

        for (Item item : this.getAllItem())
            db.delete(Util.TABLE_NAME, Util.COLUMN_ID + "=?",
                    new String[]{String.valueOf(item.getId())});
    }

    //Get count
    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        String CountQuery = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(CountQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }
}
*/

