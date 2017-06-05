package com.example.aliitani.midterm_itani;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;
import com.example.aliitani.midterm_itani.MainApp.MainApp;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;

    TextView mforgotPassword;
    TextView mSignUp;

    EditText username, password;

    String defualtUsername = "test";
    String defualtPassword = "test";
    String defaultEmail = "test@test.com";

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);

        checkLogginSession();

        mforgotPassword = (TextView) findViewById(R.id.forgot_password_id);
        mSignUp = (TextView) findViewById(R.id.sign_up_id);
        loginButton = (Button) findViewById(R.id.login_button);

        username = (EditText) findViewById(R.id.username_id);
        password = (EditText) findViewById(R.id.password_id);

    }
    public void checkLogginSession() {
        if(!mDatabaseHelper.checkIfLoggedIn().isEmpty()) {
            returnSession();
        }
    }
    public void returnSession() {
        Intent returnApp = new Intent(MainActivity.this, MainApp.class);

        returnApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        returnApp.putExtra("Username", mDatabaseHelper.checkIfLoggedIn().trim());
        startActivity(returnApp);
        finish();
    }

    public void sendData() {
        if(mDatabaseHelper.ifUserExists(username.getText().toString().trim())) {
            boolean isInserted = mDatabaseHelper.insertUserData(defualtUsername, defualtPassword, defaultEmail);
        }
    }
    public void checkCredentials(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if(username.getText().toString().trim().equals("test") && password.getText().toString().trim().equals("test")) {
            sendData();
        }
        if(databaseHelper.checkLogin(username.getText().toString(), password.getText().toString())) {
            // log in credentials worked and should start app.
            startApp();
        } else {
            Toast.makeText(this, "Wrong Username and Password try again", Toast.LENGTH_SHORT).show();
        }

    }

    public void startApp() {
        Intent startApp = new Intent(MainActivity.this, MainApp.class);

        startApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startApp.putExtra("Username", username.getText().toString());
        if(mDatabaseHelper.setUserLoginSession(username.getText().toString().trim(), password.getText().toString().trim(), 1)) {
            startActivity(startApp);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Log in aborted!", Toast.LENGTH_SHORT).show();
        }

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
