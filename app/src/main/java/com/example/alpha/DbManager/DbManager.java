package com.example.alpha.DbManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


public class DbManager
{
    private DBhelper dbhelper;
    public DbManager(Context ctx)
    {
        dbhelper=new DBhelper(ctx);
    }

    public void updateEmail(int id, String email){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DatabaseStrings.FIELD_MAIL, email);

        try {
            db.update(DatabaseStrings.TBL_NAME_USER, cv, "_id=" + id, null);
        }
        catch (SQLiteException sqle)
        {
// Gestione delle eccezioni
        }
    }


    public void updateNickname(int id, String nickname){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DatabaseStrings.FIELD_NICKNAME, nickname);

        try {
            db.update(DatabaseStrings.TBL_NAME_USER, cv, "_id=" + id, null);
        }
        catch (SQLiteException sqle)
        {
// Gestione delle eccezioni
        }
    }


    public void updatePassword(int id,String password){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DatabaseStrings.FIELD_PASSWORD, password);

        try {
            db.update(DatabaseStrings.TBL_NAME_USER, cv, "_id=" + id, null);
        }
        catch (SQLiteException sqle)
        {
// Gestione delle eccezioni
        }
    }
    public void updateUserImage(int id, String email){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DatabaseStrings.FIELD_USER_IMAGE, email);

        try {
            db.update(DatabaseStrings.TBL_NAME_USER, cv, "_id=" + id, null);
        }
        catch (SQLiteException sqle)
        {
// Gestione delle eccezioni
        }
    }

    public void saveUpdateDate(int id, String updateDate){
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DatabaseStrings.FIELD_USER_UPDATE_DATE, updateDate);
        try {
            db.update(DatabaseStrings.TBL_NAME_USER, cv, "_id=" + id, null);
        }
        catch (SQLiteException sqle)
        {
// Gestione delle eccezioni
        }
    }
    public void saveNewUser(String image, String user, String nickname, String email, String password)
    {

        if(!checkIfUserIsSaved(user)) {

            Cursor crs = null;
            try {
                SQLiteDatabase db = dbhelper.getReadableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DatabaseStrings.FIELD_USER_IMAGE, image);
                cv.put(DatabaseStrings.FIELD_USER, user);
                cv.put(DatabaseStrings.FIELD_NICKNAME, nickname);
                cv.put(DatabaseStrings.FIELD_USER_UPDATE_DATE, "00");
                cv.put(DatabaseStrings.FIELD_MAIL, email);
                cv.put(DatabaseStrings.FIELD_PASSWORD, password);
                try {
                    db.insert(DatabaseStrings.TBL_NAME_USER, null, cv);

                } catch (SQLiteException sqle) {

                }


            } catch (SQLiteException sqle) {

            }

        }else{
            SQLiteDatabase db=dbhelper.getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put(DatabaseStrings.FIELD_NICKNAME, nickname);
            cv.put(DatabaseStrings.FIELD_MAIL, email);
            cv.put(DatabaseStrings.FIELD_PASSWORD, password);

            try {
                db.update(DatabaseStrings.TBL_NAME_USER, cv, "user=" + user, null);
            }
            catch (SQLiteException sqle)
            {
// Gestione delle eccezioni
            }
        }

    }



    public boolean deleteUser(long id)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            if (db.delete(DatabaseStrings.TBL_NAME_USER, DatabaseStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0);
                return true;

        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }


    public Cursor query()
    {
        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(DatabaseStrings.TBL_NAME_USER, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }



    @SuppressLint("Range")
    public String getUserPassword(String email)
    {String[] projection = {
            DatabaseStrings.FIELD_PASSWORD
    };
        String selection = DatabaseStrings.FIELD_MAIL + " = ?";
        String[] selectionArgs = { email };



        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(DatabaseStrings.TBL_NAME_USER, projection, selection, selectionArgs, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        crs.moveToFirst();
        return crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_PASSWORD));
    }


    @SuppressLint("Range")
    public String getUserNickname(String useruid)
    {String[] projection = {
            DatabaseStrings.FIELD_NICKNAME
    };
        String selection = DatabaseStrings.FIELD_USER + " = ?";
        String[] selectionArgs = { useruid };



        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(DatabaseStrings.TBL_NAME_USER, projection, selection, selectionArgs, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        crs.moveToFirst();
        return crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_NICKNAME));
    }

    @SuppressLint("Range")
    public String getUpdateDate (String useruid)
    {String[] projection = {
            DatabaseStrings.FIELD_USER_UPDATE_DATE
    };
        String selection = DatabaseStrings.FIELD_USER + " = ?";
        String[] selectionArgs = { useruid };



        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(DatabaseStrings.TBL_NAME_USER, projection, selection, selectionArgs, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        crs.moveToFirst();
        return crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_USER_UPDATE_DATE));
    }


public boolean checkIfUserIsSaved(String userUid){
        boolean result=false;


    String[] projection = {
            DatabaseStrings.FIELD_USER
    };
    String selection = DatabaseStrings.FIELD_USER + " = ?";
    String[] selectionArgs = { userUid };



    Cursor crs=null;
    try
    {
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        crs=db.query(DatabaseStrings.TBL_NAME_USER, projection, selection, selectionArgs, null, null, null, null);
    }
    catch(SQLiteException sqle)
    {
        return false;
    }
    crs.moveToFirst();
    if(crs.getCount()!=0){
        result=true;
    }


        return result;
}
    @SuppressLint("Range")
    public String getUserImage(String useruid)
    {
        String[] projection = {
            DatabaseStrings.FIELD_USER_IMAGE
    };
        String selection = DatabaseStrings.FIELD_USER + " = ?";
        String[] selectionArgs = { useruid };



        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(DatabaseStrings.TBL_NAME_USER, projection, selection, selectionArgs, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        crs.moveToFirst();
        return crs.getString(crs.getColumnIndex(DatabaseStrings.FIELD_USER_IMAGE));
    }

    @SuppressLint("Range")
    public int getUserId(String user)
    {String[] projection = {
            DatabaseStrings.FIELD_ID
    };
        String selection = DatabaseStrings.FIELD_USER + " = ?";
        String[] selectionArgs = { user };



        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(DatabaseStrings.TBL_NAME_USER, projection, selection, selectionArgs, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return 1;
        }
        crs.moveToFirst();
        return crs.getInt(crs.getColumnIndex(DatabaseStrings.FIELD_ID));
    }

}



