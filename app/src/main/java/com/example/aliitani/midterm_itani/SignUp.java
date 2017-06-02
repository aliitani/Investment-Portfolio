package com.example.aliitani.midterm_itani;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    EditText username;
    EditText password;
    EditText email;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabaseHelper = new DatabaseHelper(this);

        username = (EditText) findViewById(R.id.username_sign_up_id);
        password = (EditText) findViewById(R.id.password_sign_up_id);
        email = (EditText) findViewById(R.id.email_sign_up_id);
        signupButton = (Button) findViewById(R.id.sign_up_id);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("sign up here");
                if(!email.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    if(validate(email.getText().toString())) {
                        if(mDatabaseHelper.ifUserExists(username.getText().toString())) {
                            // user doesnt exist add it to sendData()
                            sendData();
                        } else {
                            // user exists
                            Toast.makeText(SignUp.this, "User already exists.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // email is wrong using regex properly.
                        Toast.makeText(SignUp.this, "Re-enter your email. i.e (Example@example.com)" , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // if any editexts left empty goes here.
                    Toast.makeText(SignUp.this, "Make sure you fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendData() {
        boolean isInserted = mDatabaseHelper.insertUserData(username.getText().toString(), password.getText().toString(), email.getText().toString());

        if(isInserted) {
            Toast.makeText(SignUp.this, "Done!", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent(SignUp.this, MainActivity.class);

            // goes back to main activity properly and clearing intents
            returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
        } else {
            Toast.makeText(SignUp.this, "NOT Done! i.e Database error", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }


}
