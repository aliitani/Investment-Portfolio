package com.example.aliitani.midterm_itani.MainApp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.Database.DatabaseHelper;
import com.example.aliitani.midterm_itani.R;

import java.util.ArrayList;

/**
 * Created by aliitani on 6/4/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public Context context;
    Information infoData;
    int currentPosition;

    ArrayList<Information> data = new ArrayList<>();

    private LayoutInflater mInflater;

    private int previousPositon = 0;

    public MyAdapter (Context context, ArrayList<Information> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_row_invest, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.mtickerSymbol.setText(data.get(position).getTickerSymbol());
        myViewHolder.mNumberOfShares.setText(String.valueOf(data.get(position).getNumberOfShares()));

        currentPosition = position;
        infoData = data.get(position);


        // add or remove in here
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mtickerSymbol;
        TextView mNumberOfShares;

        public MyViewHolder(View itemView) {
            super(itemView);

            mtickerSymbol = (TextView) itemView.findViewById(R.id.ticker_symbol_view);
            mNumberOfShares = (TextView) itemView.findViewById(R.id.number_of_shares_view);
        }
    }

    public void delete(int pos) {
        data.remove(pos);
        this.notifyItemRemoved(pos);
    }
}
