package com.example.aliitani.midterm_itani.MainApp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliitani on 6/4/2017.
 */

public class Data {
    static Information mInformation = new Information();
    static ArrayList<Information> data = new ArrayList<>();

    public static void addItem(String tickerSymbol, String numberofShares, String pricePerShare, String total) {
        mInformation.setTickerSymbol(tickerSymbol);
        mInformation.setNumberOfShares(Integer.parseInt(numberofShares));
        mInformation.setPricePerShare(Double.parseDouble(pricePerShare));
        mInformation.setTotalPerShare(Double.parseDouble(total));
        data.add(mInformation);
    }
    public static ArrayList<Information> getData() {
        // intent gets user checks database add the information and then return the data.

//        for(int i = 0; i < 10; i++) {
//            Information information = new Information();
//            information.setTickerSymbol("Ticker " + i);
//            information.setNumberOfShares(i);
//            data.add(information);
//        }
        return data;
    }

}
