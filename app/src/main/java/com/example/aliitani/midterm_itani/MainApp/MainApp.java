package com.example.aliitani.midterm_itani.MainApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;
import com.example.aliitani.midterm_itani.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainApp extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    TextView titleAuthUser;
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    Data data;
    Toolbar toolbar;

    String result;
    String symbol;
    String username;

    String numberShares;
    String stockPrice;

    String tickerSymbol;

    TextView totalShares;
    double total;
//    ArrayList<Information> data;
//    Information mInformation;

    JSONObject start;
    JSONObject query;
    JSONObject results;
    JSONObject quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        databaseHelper = new DatabaseHelper(MainApp.this);
        titleAuthUser = (TextView) findViewById(R.id.authenticated_username);
        totalShares = (TextView) findViewById(R.id.total_shares);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setRecyclerView();
        setTitleAuthUser();
        setSupportActionBar(toolbar);

        onLogIn();

    }
    public void onLogIn() {
        String selectQuery = "SELECT * FROM Ip_Table where username = '1'";

    }
    public void setTitleAuthUser(){
        username = getIntent().getStringExtra("Username");
        username += "'s Investment Portfolio.";
        titleAuthUser.setText(username);

    }

    public void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mMyAdapter = new MyAdapter(MainApp.this, Data.getData());
        data = new Data();

        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Drawable dividerDrawable = ContextCompat.getDrawable(this, android.R.drawable.divider_horizontal_textfield);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

    }

    public void addItems(String price) {
        boolean isInserted = databaseHelper.insertInvestData(getIntent().getStringExtra("Username"), tickerSymbol, Integer.parseInt(numberShares), Double.parseDouble(price), (Integer.parseInt(numberShares) * Double.parseDouble(price)));
        System.out.println(isInserted);

        if (isInserted){
            data.addItem(tickerSymbol, numberShares, price, String.valueOf(Integer.parseInt(numberShares)* Double.parseDouble(price)));
            total += Integer.parseInt(numberShares) * Double.parseDouble(price);
            totalShares.setText(String.format("Total for this portfolio: %.4f", total));
            mMyAdapter.notifyItemInserted(data.getData().size()-1);
        } else {
            Toast.makeText(MainApp.this, "Note added, Database Error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void processJSON() {
        symbol = tickerSymbol;
        new BackgroundTask().execute();
    }

    public void readJSON () {
        System.out.println("here");
        System.out.println(result);

        if(result != null) {
            try {
                start = new JSONObject(result);
                query = start.getJSONObject("query");
                results = query.getJSONObject("results");
                quote = results.getJSONObject("quote");
                System.out.println(quote.getString("LastTradePriceOnly"));
                stockPrice = quote.getString("LastTradePriceOnly");
                System.out.println(stockPrice);

                addItems(stockPrice);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
          Toast.makeText(this, "JSON ERROR.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainApp.this);
        View view = getLayoutInflater().inflate(R.layout.add_item_layout_dialog, null);

        final EditText mitem = (EditText) view.findViewById(R.id.item_to_add);
        final EditText mnumbershares = (EditText) view.findViewById(R.id.number_of_shares);
        final Button addButton = (Button) view.findViewById(R.id.add_item_dialog);
        final Button closeButton = (Button) view.findViewById(R.id.cancel_button_dialog);

        builder.setTitle("Invest in more!");

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mitem.getText().toString().isEmpty() && !mnumbershares.getText().toString().isEmpty()) {
                    tickerSymbol = mitem.getText().toString().trim();
                    numberShares = mnumbershares.getText().toString().trim();
                    alertDialog.dismiss();

                    processJSON();

                } else {
                    Toast.makeText(MainApp.this, "Make sure to fill everything first", Toast.LENGTH_LONG);
                }
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void addInvestment(String symbol, double shares) {

    }

    public void refreshListItem() {
        // refresh the json and check for each user the price per shares if dropped or raised.
        // call the data abse pull tickersybmol check price from json update it in database and then update the items in recycler view
        // check for each ticker symbol the price and apply it to the number of shares and update the recycler view

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            result = s;
            readJSON();
        }

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22" + symbol + "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine()) != null) {

                    stringBuilder.append(JSON_STRING + "\n");

                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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

}
