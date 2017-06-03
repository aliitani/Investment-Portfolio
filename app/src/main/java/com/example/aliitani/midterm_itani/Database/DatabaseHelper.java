package com.example.aliitani.midterm_itani.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by aliitani on 6/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database.db";
    public static final String USERS_PROFILE_TABLE = "users_table";
    public static final String INVESTMENT_PORTFOLIO_TABLE = "ip_table";

    public static final String T1_COL1 = "ID";
    public static final String T1_COL2 = "USERNAME";
    public static final String T1_COL3 = "PASSWORD";
    public static final String T1_COL4 = "EMAIL";

    public static final String T2_COL1 = "ID";
    public static final String T2_COL2 = "USERNAME";
    public static final String T2_COL3 = "STICKER SYMBOL";
    public static final String T2_COL4 = "NUMBER OF SHARES";
    public static final String T2_COL5 = "PRICE PER SHARE";

    public static final String create_user_profile_table = "CREATE TABLE " + USERS_PROFILE_TABLE  + " (" + T1_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T1_COL2 + " TEXT, " + T1_COL3 + " TEXT, " + T1_COL4 + " TEXT" + ")";
    public static final String create_ip_table = "CREATE TABLE " + INVESTMENT_PORTFOLIO_TABLE  + " (" + T2_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T2_COL2 + " TEXT, " + T2_COL3 + " TEXT, " + T2_COL4 + " INTEGER, " + T2_COL5 + " REAL" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // used once for create the database and its tables
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_user_profile_table);
        db.execSQL(create_ip_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INVESTMENT_PORTFOLIO_TABLE);
        onCreate(db);
    }

    public boolean insertInvestData(String username, String stickerSybmol, int numberOfShares, double pricePerShare) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("USERNAME", username);
        contentValues.put("STICKER SYMBOL", stickerSybmol);
        contentValues.put("NUMBER OF SHARES", numberOfShares);
        contentValues.put("PRICE PER SHARE", pricePerShare);

        long result = db.insert(INVESTMENT_PORTFOLIO_TABLE, null, contentValues);

        if(result == -1) {
            return false;
        }

        return true;
    }

    public boolean insertUserData(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("USERNAME" , username);
        contentValues.put("PASSWORD" , password);
        contentValues.put("EMAIL" , email);

        long result = db.insert(USERS_PROFILE_TABLE, null, contentValues);

        if(result == -1) {
            return false;
        }
        return true;
    }

    public boolean ifUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + T1_COL2 + " FROM " + USERS_PROFILE_TABLE + " WHERE USERNAME = '" + username + "'";

        System.out.println(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            // means user exists
            c.close();
            return false;
        }
        // means user doesnt exist
        return true;
    }

    public boolean ifEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + T1_COL4 + " FROM " + USERS_PROFILE_TABLE + " WHERE EMAIL = '" + email + "'";

        System.out.println(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.close();
            return false;
        }
        return true;
    }
    public boolean checkLogin(String username, String password) {
        if(!ifUserExists(username)) {
            SQLiteDatabase db = this.getReadableDatabase();
            String checkUserName = "";
            String checkPassword = "";

            String selectQuery = "SELECT " +T1_COL2 +", " + T1_COL3 +" FROM " + USERS_PROFILE_TABLE + " WHERE USERNAME = '" + username +"' AND PASSWORD = '" + password+ "'";

            System.out.println(selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);

            if(c.getCount() > 0) {
                c.moveToFirst();
                checkUserName = c.getString(c.getColumnIndex(T1_COL2));
                checkPassword = c.getString(c.getColumnIndex(T1_COL3));

                c.close();
            }

            if(username.equals(checkUserName) && password.equals(checkPassword)) {
                return true;
            }
        }

        return false;
    }
    public String getPassword(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + T1_COL3 + " FROM " +  USERS_PROFILE_TABLE  + " WHERE EMAIL = '" + email + "'";

        System.out.println(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex(T1_COL3));
        }
        c.close();
        return "";
    }
}
