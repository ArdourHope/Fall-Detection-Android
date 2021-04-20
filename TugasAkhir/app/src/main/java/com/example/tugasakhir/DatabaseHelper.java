package com.example.tugasakhir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.jar.Attributes;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG= "DatabaseHelper";

    private static final String CREATE_TABLE = " create table Contact ( Name TEXT NOT NULL , Number TEXT );";


    private static final String TABLE_NAME = "Contact";
    private static final String COL1 = "Name";
    private static final String COL2 = "Number";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contact");
        onCreate(db);
    }


    public void addData (String name, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(COL1, name);
        contentValue.put(COL2, number);
        db.insert(TABLE_NAME, null, contentValue);


    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1, COL2}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void onReset(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Contact");
        onCreate(db);
    }


}
