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

    private Context context;
    Information infoData;
    int currentPosition;

    ArrayList<Information> data = new ArrayList<>();

    private LayoutInflater mInflater;

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
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        myViewHolder.mtickerSymbol.setText(data.get(position).getTickerSymbol());
        myViewHolder.mNumberOfShares.setText(String.valueOf(data.get(position).getNumberOfShares()));
        myViewHolder.mPricePerShare.setText(String.format("Price Per Share: %s", String.valueOf(data.get(position).getPricePerShare())));
        double totalPrice = (data.get(position).getNumberOfShares()*data.get(position).getPricePerShare());
        myViewHolder.mPriceshares.setText(String.format("Total Price: %.2f", totalPrice));
        currentPosition = myViewHolder.getAdapterPosition();
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
        TextView mPricePerShare;
        TextView mPriceshares;

        public MyViewHolder(View itemView) {
            super(itemView);

            mtickerSymbol = (TextView) itemView.findViewById(R.id.ticker_symbol_view);
            mNumberOfShares = (TextView) itemView.findViewById(R.id.number_of_shares_view);
            mPricePerShare = (TextView) itemView.findViewById(R.id.price_per_share_view);
            mPriceshares = (TextView) itemView.findViewById(R.id.total_shares_view);
        }
    }

    public void delete(int pos) {
        data.remove(pos);
        this.notifyItemRemoved(pos);
    }
}
