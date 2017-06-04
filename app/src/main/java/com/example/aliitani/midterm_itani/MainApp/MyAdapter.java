package com.example.aliitani.midterm_itani.MainApp;

import android.content.Context;
import android.icu.text.IDNA;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.R;

import java.util.ArrayList;

/**
 * Created by aliitani on 6/4/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public Context context;

    private ArrayList<Information> data;

    private LayoutInflater mInflater;

    private int previousPositon = 0;

    public MyAdapter (Context context, ArrayList<Information> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_invest, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.mtickerSymbol.setText(data.get(position).getTickerSymbol());
        myViewHolder.mNumberOfShares.setText(String.valueOf(data.get(position).getNumberOfShares()));

        final int currentPosition = position;
        final Information infoData = data.get(position);


        // add or remove in here
        myViewHolder.mNumberOfShares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "OnClick Called at position " + position, Toast.LENGTH_SHORT).show();
                addItem(currentPosition, infoData);
            }
        });

        myViewHolder.mtickerSymbol.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(context, "OnLongClick Called at position " + position, Toast.LENGTH_SHORT).show();

                removeItem(infoData);

                return true;
            }


        });
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

    private void removeItem(Information infoData) {
        int currentPosition = data.indexOf(infoData);
        data.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }
    private void addItem(int position, Information infoData) {
        data.add(position, infoData);
        notifyItemInserted(position);
    }
}
