package com.example.sezar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sezar.model.ReadMessageItem;

public class DBReadMessageHelperLocalMessage extends SQLiteOpenHelper {

    private static final String Log_Message = "filter";
    private static final String DB_FILE_NAME = "sezarLocal.db";
    private static final int DB_VERSION = 43;
    private static final String TABLE_ITEMS = "r";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ENCRYPTED_MESSAGE = "encryptedmessage";
    private static final String COLUMN_DELETE_ADAPTER_LIST = "DeleteAdapter";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_PHONE_NUMBER = "phonenumberlocal";
    private static final String COLUMN_READ_STATE = "readstate";
    private static final String COLUMN_CIPHERING_KEY = "cipheringkey";


    public DBReadMessageHelperLocalMessage(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);

    }

    public DBReadMessageHelperLocalMessage(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemsReadMessage.SQL_CREATE);
        //db.execSQL(ItemsReadMessage.SQL_CREATE_PHONE);
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
            String query = "SELECT * FROM r"; // No trailing ';'
            cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }




    public void deletedata() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
    }



    public void deleteEntry(long row) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + "=" + row, null);
        //db.close();
    }

    public void updateData(ReadMessageItem item, long row) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ENCRYPTED_MESSAGE, item.getEncryptedMessage());
        contentValues.put(COLUMN_DELETE_ADAPTER_LIST, item.getDeleteAdapterList());
        contentValues.put(COLUMN_PHONE_NUMBER, item.getPhoneNumber());
        contentValues.put(COLUMN_MESSAGE, item.getMessage());
        contentValues.put(COLUMN_READ_STATE, item.getReadState());
        contentValues.put(COLUMN_CIPHERING_KEY, item.getCipheringKey());
        long result = db.update(TABLE_ITEMS, contentValues, COLUMN_ID + "=" + row, null);
        db.close();
    }

    public void insertdata(ReadMessageItem item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ENCRYPTED_MESSAGE, item.getEncryptedMessage());
            //  contentValues.put(COLUMN_DELETE_ADAPTER_LIST, item.getDeleteAdapterList());
            contentValues.put(COLUMN_MESSAGE, item.getMessage());
            contentValues.put(COLUMN_PHONE_NUMBER, item.getPhoneNumber());
            contentValues.put(COLUMN_READ_STATE, item.getReadState());
            contentValues.put(COLUMN_CIPHERING_KEY, item.getCipheringKey());
            long result = db.insert(TABLE_ITEMS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // db.close();
    }



}



