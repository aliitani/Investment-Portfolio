package com.example.aliitani.midterm_itani.RecyclerView;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliitani.midterm_itani.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), android.R.drawable.divider_horizontal_textfield);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        updateUI();

        return v;
    }
    private void updateUI () {
        ItemList itemList = ItemList.get(getActivity());
        List<Item> items = itemList.getItems();

        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Item mitem;

        public TextView mInvestTextView;
        public TextView mNumberOfShares;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //mInvestTextView = (TextView) itemView.findViewById(R.id.ticker_symbol_view);
            mNumberOfShares = (TextView) itemView.findViewById(R.id.number_of_shares_view);
        }

        public void bindItem(Item item) {
            mitem = item;
//            mInvestTextView.setText(mitem.getTickerSymbol());
            System.out.println(mitem.getNumberOfShares() + " + " + mitem.getTickerSymbol());
            mNumberOfShares.setText(String.valueOf(mitem.getNumberOfShares()));
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mitem.getTickerSymbol() + " clicked!" , Toast.LENGTH_SHORT).show();
        }
    }
    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Item> mItemList;

        public RecyclerViewAdapter(List<Item> items) {
            mItemList = items;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_invest, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            Item item = mItemList.get(position);
            holder.bindItem(item);
           // holder.mTextView.setText(item.getNumberOfShares());
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

    }
}
