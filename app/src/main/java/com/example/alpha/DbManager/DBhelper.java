package com.example.alpha.DbManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper
{
    public static final String DBNAME="PROJECTCANDADB";
    public DBhelper(Context context) {
        super(context, DBNAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String q="CREATE TABLE IF NOT EXISTS "+DatabaseStrings.TBL_NAME_USER+
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_USER_IMAGE+" TEXT," +
                DatabaseStrings.FIELD_USER+" TEXT," +
                DatabaseStrings.FIELD_NICKNAME+" TEXT," +
                DatabaseStrings.FIELD_USER_UPDATE_DATE+" TEXT," +
                DatabaseStrings.FIELD_MAIL+" TEXT," +
                DatabaseStrings.FIELD_PASSWORD+" TEXT)";
        db.execSQL(q);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    { }
}