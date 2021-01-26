package com.example.sezar.database;

public class ItemsLoginScreen {
    private static final String TABLE_ITEMS = "loginScreen";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_NAME= "username";
    private static final String COLUMN_PASSWORD= "password";
    private static final String COLUMN_DEVICE_ID= "deviceId";
    private static final String COLUMN_CIPHERING_KEY = "cipheringkey";






    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "(" +
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_USER_NAME+" TEXT,"+
                    COLUMN_PASSWORD+" TEXT,"+
                    COLUMN_DEVICE_ID+" TEXT,"+
                    COLUMN_CIPHERING_KEY +" TEXT" + ");";


    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;

}
