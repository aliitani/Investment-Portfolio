package com.example.aliitani.midterm_itani;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseHelper;
import database.DatabaseSchema.DBTable;

public class MainActivity extends AppCompatActivity {
    public Context mContext;
    public SQLiteDatabase mDatabase;

    TextView mforgotPassword;
    TextView mSignUp;

    public MainActivity(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mforgotPassword = (TextView) findViewById(R.id.forgot_password_id);
        mSignUp = (TextView) findViewById(R.id.sign_up_id);

    }

    public void forgotPassword(View view) {
        Intent forgotPassword = new Intent(MainActivity.this, ForgotPassword.class);
        startActivity(forgotPassword);
    }
    public void setSignUp(View v) {
        Intent signUp = new Intent(MainActivity.this, SignUp.class);
        startActivity(signUp);
    }
}
