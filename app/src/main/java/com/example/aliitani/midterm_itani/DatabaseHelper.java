package com.example.aliitani.midterm_itani;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aliitani on 6/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MidTerm.db";
    public static final String TABLE_NAME = "DBTABLE";
    public static final String COL1 = "ID";
    public static final String COL2 = "USERNAME";
    public static final String COL3 = "PASSWORD";
    public static final String COL4 = "EMAIL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME  + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2+ " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
