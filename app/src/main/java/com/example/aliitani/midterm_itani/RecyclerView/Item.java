package com.example.aliitani.midterm_itani.RecyclerView;

import java.util.Date;

/**
 * Created by aliitani on 6/3/2017.
 */

public class Item {
    private String tickerSymbol;
    private int numberOfShares;
    private double pricePerShare;

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public void setPricePerShare(double pricePerShare) {
        this.pricePerShare = pricePerShare;
    }
}
