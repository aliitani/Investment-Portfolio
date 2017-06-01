package com.example.aliitani.midterm_itani;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void forgotPassword() {
        Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_LONG).show();
    }
}
