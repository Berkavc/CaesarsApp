package com.example.sezar.database;

public class ItemsLoginScreenCheckBoxTable {
    public static final String TABLE_ITEMS = "names";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_CIPHERING_KEY = "cipheringkey";



    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_USERNAME+" TEXT,"+
                    COLUMN_CIPHERING_KEY +" TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;

}
