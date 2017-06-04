package com.example.aliitani.midterm_itani.RecyclerView;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliitani on 6/3/2017.
 */

public class ItemList {
    private static ItemList sItemList;
    private List<Item> mItems;

    public static ItemList get(Context context) {
        if(sItemList == null) {
            sItemList = new ItemList(context);
        }
        return sItemList;
    }
    private ItemList(Context context) {
        mItems = new ArrayList<>();
        // generate random items
//        for(int i = 0; i < 10; i++) {
//            Item item = new Item();
//            item.setTickerSymbol("Ticker " + i);
//            item.setPricePerShare(i);
//            item.setNumberOfShares(i);
//            mItems.add(item);
//
//        }
    }
    public List<Item> getItems() {
        return mItems;
    }

    public Item getItem(String title) {
        for(Item item: mItems) {
            if(item.getTickerSymbol().equals(title)) {
                return item;
            }
        }
        return null;
    }

    //genrerate random items;

}
