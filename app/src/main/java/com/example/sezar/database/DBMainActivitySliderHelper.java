package com.example.sezar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sezar.model.LoginScreenCheckBoxItem;
import com.example.sezar.model.MainActivitySliderItem;

public class DBMainActivitySliderHelper extends SQLiteOpenHelper {

    private static final String Log_Message = "filter";
    private static final String DB_FILE_NAME = "sezarslider.db";
    private static final int DB_VERSION = 2;
    public static final String TABLE_ITEMS = "safety";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SAFETY_STATE = "safetystate";



    public DBMainActivitySliderHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);

    }

    public DBMainActivitySliderHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemsMainActivitySlider.SQL_CREATE);
        Log.i(Log_Message, "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public Cursor getAllContacts() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM safety"; // No trailing ';'
            cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }


    public void deletedata(MainActivitySliderItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
    }

    public void deleteEntry(long row) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + "=" + row, null);
        db.close();
    }

    public void updateData(MainActivitySliderItem item, long row) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SAFETY_STATE, item.getSafetyState());
        long result = db.update(TABLE_ITEMS, contentValues, COLUMN_ID + "=" + row, null);
       // db.close();
    }

    public void insertdata(MainActivitySliderItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SAFETY_STATE, item.getSafetyState());
        long result = db.insert(TABLE_ITEMS, null, contentValues);
        //db.close();
    }



}



