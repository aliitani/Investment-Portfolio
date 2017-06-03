package com.example.aliitani.midterm_itani.RecyclerView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;
import com.example.aliitani.midterm_itani.R;

import org.w3c.dom.Text;

public class RecyclerActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        databaseHelper = new DatabaseHelper(this);
        TextView titleAuthUser = (TextView) findViewById(R.id.authenticated_username);

        String username = getIntent().getStringExtra("Username");


        username += "'s Investment Portfolio.";

        titleAuthUser.setText(username);
    }
}
