package com.example.sezar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.sezar.model.CreateAccountItem;

public class DBCreateAccountHelper extends SQLiteOpenHelper {

    private static final String Log_Message = "filter";
    private static final String DB_FILE_NAME = "sezar.db";
    private static final int DB_VERSION = 3;
    private static final String TABLE_ITEMS = "videos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_VIDEO = "video";


    public DBCreateAccountHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);

    }

    public DBCreateAccountHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemsAccountTable.SQL_CREATE);
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
            String query = "SELECT * FROM videos"; // No trailing ';'
            cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }


    public void deletedata(CreateAccountItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
    }

    public void deleteEntry(long row) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + "=" + row, null);
        db.close();
    }

    public void updateData(CreateAccountItem item, long row) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VIDEO, item.getEmail());
        long result = db.update(TABLE_ITEMS, contentValues, COLUMN_ID + "=" + row, null);
        db.close();
    }

    public void insertdata(CreateAccountItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VIDEO, item.getEmail());
        long result = db.insert(TABLE_ITEMS, null, contentValues);
        db.close();
    }

}



