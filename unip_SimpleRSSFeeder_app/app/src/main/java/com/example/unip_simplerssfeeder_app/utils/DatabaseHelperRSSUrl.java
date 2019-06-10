package com.example.unip_simplerssfeeder_app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperRSSUrl extends SQLiteOpenHelper {


    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "rss_url_table";
    private static final String COL_ID = "rss_url_id";
    private static final String COL_URL = "rss_url";
    private static final String createTable = "CREATE TABLE " + TABLE_NAME +
            " ( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_URL +" TEXT)";

    //__________________________________ public DatabaseHelperRSSUrl(Context context) { _______________
    public DatabaseHelperRSSUrl(Context context) {
        super(context, TABLE_NAME, null, 1);
    }
    //_____________________________________ public void onCreate(SQLiteDatabase db) { ___________________
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }
    // ________________________public void onUpgrade(SQLiteDatabase db, int i, int i1) {_________________
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //_____________________________ public boolean addURLToDB(String item) {  ______________________
    public boolean addURLToDB(String elem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_URL, elem);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        //if elem as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //_____________________________ public Cursor getURLs(){   ____________________________________
    public Cursor getURLs(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COL_URL+" FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        return data;
    }
    //_____________________________ public void deleteURL(String recordName){  _____________________
    public void deleteURL(String recordName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL_URL + " = '" + recordName + "'";
        db.execSQL(query);

    }
    //_____________________________  public boolean checkIfurlExist(String recordName){______________
    public boolean checkIfurlExist(String recordName){
        SQLiteDatabase  db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+COL_URL+" =  '" + recordName + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();

        return exist;
    }


}
