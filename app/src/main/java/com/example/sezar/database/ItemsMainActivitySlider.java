package com.example.sezar.database;

public class ItemsMainActivitySlider {
    public static final String TABLE_ITEMS = "safety";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SAFETY_STATE = "safetystate";



    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_SAFETY_STATE +" TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;

}
