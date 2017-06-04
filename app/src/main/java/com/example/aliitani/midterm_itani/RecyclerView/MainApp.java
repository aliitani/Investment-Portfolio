package com.example.aliitani.midterm_itani.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;
import com.example.aliitani.midterm_itani.R;

public class MainApp extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Item item;
    TextView titleAuthUser;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        databaseHelper = new DatabaseHelper(this);
        titleAuthUser = (TextView) findViewById(R.id.authenticated_username);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        String username = getIntent().getStringExtra("Username");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment);

        if(fragment == null) {
            fragment = new RecyclerViewFragment();
            fm.beginTransaction().add(R.id.fragment, fragment)
                    .commit();
        }

        username += "'s Investment Portfolio.";

        titleAuthUser.setText(username);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                addItem();
                return true;
            case R.id.refresh_item:
                refreshListItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainApp.this);
        View view = getLayoutInflater().inflate(R.layout.add_item_layout_dialog, null);

        EditText mitem = (EditText) view.findViewById(R.id.item_to_add);
        EditText mnumbershares = (EditText) view.findViewById(R.id.number_of_shares);


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("dsajfhasdlkfsad");
            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        Toast.makeText(this, "geere", Toast.LENGTH_SHORT).show();
    }

    public void addInvestment(String symobl, double shares) {

    }

    public void refreshListItem() {

    }
}
