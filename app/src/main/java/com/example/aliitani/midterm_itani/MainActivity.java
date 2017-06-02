package com.example.aliitani.midterm_itani;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;

    TextView mforgotPassword;
    TextView mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);

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
