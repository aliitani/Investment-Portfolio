package com.example.aliitani.midterm_itani.Database;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aliitani.midterm_itani.MainApp.Information;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aliitani on 6/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database.db";
    public static final String USERS_PROFILE_TABLE = "Users_Table";
    public static final String INVESTMENT_PORTFOLIO_TABLE = "Ip_Table";
    public static final String USER_LOGIN_SESSION = "Users_LOGIN";

    public static final String T1_COL1 = "ID";
    public static final String T1_COL2 = "USERNAME";
    public static final String T1_COL3 = "PASSWORD";
    public static final String T1_COL4 = "EMAIL";

    public static final String T2_COL1 = "ID";
    public static final String T2_COL2 = "USERNAME";
    public static final String T2_COL3 = "STICKER";
    public static final String T2_COL4 = "NUMBER";
    public static final String T2_COL5 = "PRICE";
    public static final String T2_COL6 = "TOTAL";

    public static final String T3_COL1 = "ID";
    public static final String T3_COL2 = "USERNAME";
    public static final String T3_COL3 = "PASSWORD";
    public static final String T3_COL4 = "ISLOGGEDIN";

    public static final String create_user_profile_table = "CREATE TABLE " + USERS_PROFILE_TABLE  + " (" + T1_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T1_COL2 + " TEXT, " + T1_COL3 + " TEXT, " + T1_COL4 + " TEXT" + ")";
    public static final String create_ip_table = "CREATE TABLE " + INVESTMENT_PORTFOLIO_TABLE  + " (" + T2_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T2_COL2 + " TEXT, " + T2_COL3 + " TEXT, " + T2_COL4 + " INTEGER, " + T2_COL5 + " REAL, " +  T2_COL6 + " REAL" + ")";
    public static final String create_userLogin_table = "CREATE TABLE " + USER_LOGIN_SESSION + " (" + T3_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T3_COL2 + " TEXT, " + T3_COL3 + " TEXT, " + T3_COL4 + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
        Log.d("Database Operations", "Database Created!");
        //db version
        // used once for create the database and its tables
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_user_profile_table);
        db.execSQL(create_ip_table);
        db.execSQL(create_userLogin_table);
        Log.d("Database Operations" , "Table Created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_PROFILE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INVESTMENT_PORTFOLIO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_LOGIN_SESSION);
        onCreate(db);
        Log.d("Database Operations", "Table Updated..");
    }
    public boolean checkforDuplicates(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT DISTINCT STICKER FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public int getTotaLNumber(String ticker, String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT DISTINCT NUMBER FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "' AND STICKER = '" + ticker + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            int answer = Integer.parseInt(c.getString(c.getColumnIndex("NUMBER")));
            System.out.println("here"  + answer);
            return answer;
        }
        c.close();
        return 0;
    }

    public double getTotalPerShare(String ticker, String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT DISTINCT TOTAL FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "' AND STICKER = '" + ticker + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            double answer = Double.parseDouble(c.getString(c.getColumnIndex("TOTAL")));
            System.out.println("here"  + answer);
            return answer;
        }
        c.close();
        return 0;
    }

    public boolean setUserLoginSession(String username, String password, int state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", password);
        contentValues.put("ISLOGGEDIN", state);
        long result = db.insert(USER_LOGIN_SESSION, null, contentValues);

        if(result == -1) {
            return false;
        }
        Log.d("Database Operations", " One Row Inserted Login Session");
        return true;
    }

    public void LogOut(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + USER_LOGIN_SESSION + " SET ISLOGGEDIN = '" + 0 + "' WHERE USERNAME = '" + username + "'");
        db.close();
    }

    public String checkIfLoggedIn() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + T3_COL2 + " FROM " + USER_LOGIN_SESSION + " WHERE ISLOGGEDIN = '" + 1 + "'";
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex("USERNAME"));
        }
        return "";
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
        Log.d("Database Operations", "One Row Inserted..");
        return true;
    }

    public Cursor getInformation(SQLiteDatabase db) {
        String[] projection = {"STICKER", "NUMBER", "PRICE", "TOTAL"};

        Cursor cursor = db.query(INVESTMENT_PORTFOLIO_TABLE, projection, null,null,null,null,null,null);

        return cursor;
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

    public boolean onLogIn(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "'";

        System.out.println(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            return true;
        }
        c.close();
        return false;
    }

    public int getUserCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "'";

        System.out.println(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount() > 0) {
            return c.getCount();
        }
            return 0;
    }

    public ArrayList<HashMap<String, String>> getItems(String username) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
//        ArrayList<Information> arrayList = new ArrayList<Information>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "'";

        System.out.println(selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        System.out.println(c.getCount());
        if(c.getCount() > 0) {
            while(!c.isAfterLast()) {
                HashMap<String, String> todo = new HashMap<String, String>();
                todo.put("STICKER", c.getString(2));
                todo.put("NUMBER", c.getString(3));
                todo.put("PRICE", c.getString(4));
                todo.put("TOTAL", c.getString(5));

                arrayList.add(todo);

                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return arrayList;
    }

    public String getID(String username, String tickerSymbol, String numberOfShares) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT ID FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "' AND STICKER = '" + tickerSymbol + "' AND NUMBER = '" + numberOfShares + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            return c.getString(c.getColumnIndex("ID"));
        }
        return "";
    }

    public Integer deleteData(String username, String tickerSymbol, int numberOfShares) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String id = getID(username, tickerSymbol, String.valueOf(numberOfShares));
        if(id.isEmpty()) {
            return 0;
        }else {
            return sqLiteDatabase.delete(INVESTMENT_PORTFOLIO_TABLE, "STICKER = ? AND NUMBER = ? AND ID = ?" , new String[] {tickerSymbol, String.valueOf(numberOfShares), id});
        }
    }

    public boolean insertInvestData(String username, String stickerSymbol, int numberOfShares, double pricePerShare, double totalPerShare) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("STICKER", stickerSymbol);
        contentValues.put("NUMBER", numberOfShares);
        contentValues.put("PRICE", pricePerShare);
        contentValues.put("TOTAL", totalPerShare);

        long result = db.insert(INVESTMENT_PORTFOLIO_TABLE, null, contentValues);

        if(result == -1) {
            return false;
        }



        return true;
    }

    public boolean checkIfDifferent(String username, String stickerSymbol, String numberShares, String stockPrice) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT PRICE FROM " + INVESTMENT_PORTFOLIO_TABLE + " WHERE USERNAME = '" + username + "' AND STICKER = '" + stickerSymbol +"' AND NUMBER = '" + numberShares + "';";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        if(c.getCount() > 0) {
            if(c.getString(c.getColumnIndex("PRICE")).equals(stockPrice)) {
                return true;
            }
        }

        return false;
    }

    public void updateItems(String username, String stickerSymbol, String numberShares, String stockPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(!checkIfDifferent(username,stickerSymbol,numberShares,stockPrice)) {
            double total = Integer.parseInt(numberShares) * Double.parseDouble(stockPrice);
            db.execSQL("UPDATE " + INVESTMENT_PORTFOLIO_TABLE + " SET PRICE = '" + stockPrice + "', TOTAL = '" + total + "' WHERE USERNAME ='" + username + "' AND STICKER = '" + stickerSymbol + "'");
            db.close();
        }
    }
}
