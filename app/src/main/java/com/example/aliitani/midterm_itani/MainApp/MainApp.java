package com.example.aliitani.midterm_itani.MainApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;
import com.example.aliitani.midterm_itani.MainActivity;
import com.example.aliitani.midterm_itani.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainApp extends AppCompatActivity {
    DatabaseHelper databaseHelper;


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyAdapter mMyAdapter;

    Toolbar toolbar;
    TextView titleAuthUser;

    String result;
    String symbol;
    String username;

    String numberShares;
    String stockPrice;

    String tickerSymbol;

    TextView totalShares;
    double total = 0.0;

    String newNumberShares;

    JSONObject start;
    JSONObject query;
    JSONObject results;
    JSONObject quote;
    Information mInformation;
    ArrayList<Information> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        databaseHelper = new DatabaseHelper(MainApp.this);
        titleAuthUser = (TextView) findViewById(R.id.authenticated_username);
        totalShares = (TextView) findViewById(R.id.total_shares);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        mInformation = new Information();
        data = new ArrayList<>();

        setRecyclerView();
        setTitleAuthUser();
        setItemTouchHelper();
        setSupportActionBar(toolbar);
    }

    public void setTitleAuthUser(){
        username = getIntent().getStringExtra("Username");
        username += "'s Investment Portfolio.";
        titleAuthUser.setText(username);

    }

    public void addItem(String tickerSymbol, String numberofShares, String pricePerShare, String total) {
        mInformation.setTickerSymbol(tickerSymbol);
        mInformation.setNumberOfShares(Integer.parseInt(numberofShares));
        mInformation.setPricePerShare(Double.parseDouble(pricePerShare));
        mInformation.setTotalPerShare(Double.parseDouble(total));
        data.add(mInformation);
    }

    public ArrayList<Information> getData() {
        return data;
    }

    public ArrayList<Information> returnOnStart() {
        data = new ArrayList<>();
        databaseHelper = new DatabaseHelper(MainApp.this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = databaseHelper.getInformation(sqLiteDatabase);

        cursor.moveToFirst();
        do {
            Information information = new Information();
            information.setTickerSymbol(cursor.getString(0));
            information.setNumberOfShares(Integer.parseInt(cursor.getString(1)));
            information.setPricePerShare(Double.parseDouble(cursor.getString(2)));
            information.setTotalPerShare(Double.parseDouble(cursor.getString(3)));
            data.add(information);
        } while (cursor.moveToNext());
        databaseHelper.close();
        return data;
    }

    public void getTotalValues() {
        if(databaseHelper.onLogIn(getIntent().getStringExtra("Username"))) {
            ArrayList<HashMap<String, String>> arrayList = databaseHelper.getItems(getIntent().getStringExtra("Username"));
            total = 0;
            for(int i = 0; i < arrayList.size(); i++){

                total += Double.parseDouble(arrayList.get(i).get("TOTAL"));
            }

            totalShares.setText(String.format("Total Investments: %.2f", total));
        }
    }
    public void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(databaseHelper.onLogIn(getIntent().getStringExtra("Username"))) {
            ArrayList<HashMap<String, String>> arrayList = databaseHelper.getItems(getIntent().getStringExtra("Username"));

            total = 0.0;

            for(int i = 0; i < arrayList.size(); i++){

                total += Double.parseDouble(arrayList.get(i).get("TOTAL"));
            }

            totalShares.setText(String.format("Total Investments: %.2f", total));
            mMyAdapter = new MyAdapter(MainApp.this, returnOnStart());
        } else {
            mMyAdapter = new MyAdapter(MainApp.this, getData());
        }
        mRecyclerView.setAdapter(mMyAdapter);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, android.R.drawable.divider_horizontal_textfield);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    }

    public void addItems(String price) {
        boolean isInserted = databaseHelper.insertInvestData(getIntent().getStringExtra("Username"), tickerSymbol, Integer.parseInt(numberShares), Double.parseDouble(price), (Integer.parseInt(numberShares) * Double.parseDouble(price)));
        System.out.println(isInserted);

        if (isInserted){
            addItem(tickerSymbol, numberShares, price, String.valueOf(Integer.parseInt(numberShares)* Double.parseDouble(price)));
            total += (Integer.parseInt(numberShares) * Double.parseDouble(price));
            totalShares.setText(String.format("Total Investments: %.2f", total));
            mMyAdapter.notifyItemInserted(getData().size()-1);
            setRecyclerView();
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

    public void readNewJSON() {
        Log.d("JSON Operations", "Reading New JSON");
        if(result != null) {
            try {
                start = new JSONObject(result);
                query = start.getJSONObject("query");
                results = query.getJSONObject("results");
                quote = results.getJSONObject("quote");
                System.out.println(quote.getString("LastTradePriceOnly"));
                stockPrice = quote.getString("LastTradePriceOnly");
                System.out.println(stockPrice);

                Log.d("JSON Operations", "Done Reading!");
                updateItems(stockPrice);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "JSON ERROR.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateItems(String stockPrice) {
        databaseHelper.updateItems(getIntent().getStringExtra("Username"), symbol, newNumberShares, stockPrice);
        setRecyclerView();
        Toast.makeText(MainApp.this, "Done Updating", Toast.LENGTH_SHORT).show();
    }

    public void refreshListItem() {
        // refresh the json and check for each user the price per shares if dropped or raised.
        // call the data abse pull tickersybmol check price from json update it in database and then update the items in recycler view
        // check for each ticker symbol the price and apply it to the number of shares and update the recycler view
        ArrayList<HashMap<String, String>> array = databaseHelper.getItems(getIntent().getStringExtra("Username"));
        if(array.size() != 0) {
            for(int i = 0; i <array.size(); i++) {
                symbol = array.get(i).get("STICKER");
                newNumberShares = array.get(i).get("NUMBER");
                new RefreshBackgroundTask().execute();
            }
        } else {
            Toast.makeText(MainApp.this, "Nothing to Update", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItemDialog() {
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

    public void deleteItem(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainApp.this);
        View view = getLayoutInflater().inflate(R.layout.delete_item_slide, null);

        final Button okButton = (Button) view.findViewById(R.id.ok_button);
        final Button closeButton = (Button) view.findViewById(R.id.no_button);

        builder.setTitle("Delete an item!!");
        builder.setMessage("Are you sure?!?!");

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deleteRos = databaseHelper.deleteData(getIntent().getStringExtra("Username"), data.get(position).getTickerSymbol(), data.get(position).getNumberOfShares());
                if(deleteRos > 0) {
                    total -= data.get(position).getTotalPerShare();
                    System.out.println(databaseHelper.getTotalPerShare(data.get(position).getTickerSymbol(), getIntent().getStringExtra("Username")));
                    totalShares.setText(String.format("Total Investments: %s", String.valueOf(total)));
                    data.remove(position);
                    mMyAdapter.notifyItemRemoved(position);
                    Toast.makeText(MainApp.this, "Deletion is Done!", Toast.LENGTH_SHORT).show();
                    getTotalValues();
                } else {
                    Toast.makeText(MainApp.this, "Deletion is Not Done!", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
                databaseHelper.close();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerView();
                alertDialog.dismiss();
            }
        });

    }

    public void moveItem(int oldPos, int newPos) {
        Information temp = data.get(oldPos);
        data.remove(oldPos);
        data.add(newPos, temp);
        mMyAdapter.notifyItemMoved(oldPos, newPos);
    }

    public void setItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleItemCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());

            }
        };
        return simpleItemCallback;
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;
        ProgressDialog mProgressDialog;
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            result = s;
            mProgressDialog.dismiss();
            readJSON();
        }

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22" + symbol + "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
            mProgressDialog = new ProgressDialog(MainApp.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("Please wait..");
            mProgressDialog.setMessage("Fetch in Progress");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
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
                    Thread.sleep(500);
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

    class RefreshBackgroundTask extends AsyncTask<Void, Void, String> {
        String JSON_URL;
        String JSON_STRING;
        ProgressDialog mProgressDialog;
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            result = s;
            mProgressDialog.dismiss();
            readNewJSON();
        }

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://query.yahooapis.com/v1/public/yql?q=Select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22" + symbol + "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
            mProgressDialog = new ProgressDialog(MainApp.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("Please wait..");
            mProgressDialog.setMessage("Update in Progress");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
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
                    Thread.sleep(500);
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

    public void logOut() {
        databaseHelper.LogOut(getIntent().getStringExtra("Username").trim());
        Intent logoutIntent = new Intent(MainApp.this, MainActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logoutIntent);
        Toast.makeText(this, "Log out done!", Toast.LENGTH_LONG).show();
        finish();
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
                addItemDialog();
                return true;
            case R.id.refresh_item:
                refreshListItem();
                return true;
            case R.id.logout_item:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
