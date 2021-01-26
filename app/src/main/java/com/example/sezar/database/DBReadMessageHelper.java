package com.example.sezar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sezar.model.ReadMessageItem;

public class DBReadMessageHelper extends SQLiteOpenHelper {

    private static final String Log_Message = "filter";
    private static final String DB_FILE_NAME = "sezarRead.db";
    private static final int DB_VERSION = 12;
    private static final String TABLE_ITEMS = "readMessage";
    private static final String TABLE_ITEMS_PHONE = "readMessagephone";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHONE_NUMBER = "phonenumberlocal";
    private static final String COLUMN_CIPHERING_KEY = "cipheringkey";




    public DBReadMessageHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);

    }

    public DBReadMessageHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(ItemsReadMessage.SQL_CREATE);
        db.execSQL(ItemsReadMessage.SQL_CREATE_PHONE);
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


    public Cursor getAllContactsPhone() {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM readMessagePhone"; // No trailing ';'
            cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }


    /*public void deletedata() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
    }*/


    public void deletedataPhone() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS_PHONE, null, null);
    }

    public void deleteEntry(long row) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + "=" + row, null);
        //db.close();
    }



    public void insertdataPhone(ReadMessageItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PHONE_NUMBER,item.getPhoneNumber());
        contentValues.put(COLUMN_CIPHERING_KEY,item.getCipheringKey());
        long result = db.insert(TABLE_ITEMS_PHONE, null, contentValues);
        // db.close();
    }

}



