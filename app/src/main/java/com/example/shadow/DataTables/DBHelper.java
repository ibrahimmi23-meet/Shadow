package com.example.shadow.DataTables;



import static com.example.shadow.DataTables.QueryString.*;
import static com.example.shadow.DataTables.TablesString.ProductTable.*;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

public  class DBHelper {
    private static final String DATABASE_NAME = "MyProject.db";
    private static final int DATABASE_VERSION = 1;
    private static final String IMAGE = "image";

    private Context mContext;
    private DataBaseHelper dbhelper;
    private SQLiteDatabase db;

    private class DataBaseHelper extends SQLiteOpenHelper {
        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_PRODUCT);
            onCreate(sqLiteDatabase);
        }

    }
    public void Reset(){
        dbhelper.onUpgrade(db,1,1);
    }
    public DBHelper(Context context){
        mContext = context;
        dbhelper = new DataBaseHelper(mContext);
    }
    public DBHelper OpenWriteAble() throws SQLException{
        db = dbhelper.getWritableDatabase();
        return this;
    }
    public DBHelper OpenReadAble() throws SQLException{
        db = dbhelper.getReadableDatabase();
        return this;
    }

    public void Close(){
        dbhelper.close();
    }
    public void InsertImage(byte[] imageByte){
        ContentValues cv = new ContentValues();
        cv.put(IMAGE,imageByte);
    }
    public byte[] RetriveImageFromDB(){
        String[] projection = {
                BaseColumns._ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_STOCK,
                COLUMN_PRODUCT_SALEPRICE,
                COLUMN_PRODUCT_BUYPRICE
        };
        /*String selection = COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };*/

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                BaseColumns._ID + " DESC";
        Cursor c = db.query(true,TABLE_PRODUCT,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                "1");

        if(c.moveToFirst()){
            byte[] blob = c.getBlob(c.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE));
            c.close();
            return blob;
        }
        c.close();
        return null;
    }

}


