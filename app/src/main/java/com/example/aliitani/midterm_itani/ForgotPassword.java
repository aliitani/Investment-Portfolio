package com.example.aliitani.midterm_itani;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;

import java.util.regex.Matcher;

import static com.example.aliitani.midterm_itani.SignUp.VALID_EMAIL_ADDRESS_REGEX;

public class ForgotPassword extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    EditText email;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mDatabaseHelper = new DatabaseHelper(this);
        email = (EditText) findViewById(R.id.email);
        submitButton = (Button) findViewById(R.id.get_password);

        // check if email exists in data base and also check if it meets regex.
        // 1 check if meets regex
        // 2 check if exists in db

    }

    public void sendEmail(View view) {
         if (!email.getText().toString().isEmpty()) {
             if(validate(email.getText().toString())) {
                 Intent i = new Intent(Intent.ACTION_SEND);
                 i.setType("message/rfc822");
                 //SQL HERE
                 if (!mDatabaseHelper.ifEmailExists(email.getText().toString())) {
                     i.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});

                     i.putExtra(Intent.EXTRA_SUBJECT, "Forgot Password?!");
                     //SQL HERE
                     i.putExtra(Intent.EXTRA_TEXT, mDatabaseHelper.getPassword(email.getText().toString()));


                     try {
                         startActivity(Intent.createChooser(i, "Send mail..."));
                     } catch (android.content.ActivityNotFoundException ex) {
                         Toast.makeText(ForgotPassword.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                     }
                 } else {
                     Toast.makeText(this, "Email does not exist!", Toast.LENGTH_SHORT).show();
                 }
             } else {
                 Toast.makeText(ForgotPassword.this, "Check email i.e example@example.com", Toast.LENGTH_SHORT).show();
             }
        } else {
            Toast.makeText(ForgotPassword.this, "Enter an email first.", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

}
