package com.example.aliitani.midterm_itani;

import android.content.Intent;
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

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);

        mforgotPassword = (TextView) findViewById(R.id.forgot_password_id);
        mSignUp = (TextView) findViewById(R.id.sign_up_id);
        loginButton = (Button) findViewById(R.id.login_button);

        username = (EditText) findViewById(R.id.username_id);
        password = (EditText) findViewById(R.id.password_id);

    }
    public void checkCredentials(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        if(databaseHelper.checkLogin(username.getText().toString(), password.getText().toString())) {
            Intent startApp = new Intent(MainActivity.this, MainApp.class);

            // log in credentials worked and should start app.
            startApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startApp.putExtra("Username", username.getText().toString());
            startActivity(startApp);
            finish();
        } else {
            Toast.makeText(this, "Wrong Username and Password try again", Toast.LENGTH_SHORT).show();
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
