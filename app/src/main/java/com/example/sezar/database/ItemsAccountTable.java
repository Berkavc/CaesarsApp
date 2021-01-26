package com.example.sezar.database;

public class ItemsAccountTable {
    public static final String TABLE_ITEMS = "videos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_VIDEO = "video";




    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS  " + TABLE_ITEMS + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_VIDEO +" TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;

}
