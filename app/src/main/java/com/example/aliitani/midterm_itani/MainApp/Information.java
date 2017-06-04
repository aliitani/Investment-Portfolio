package com.example.aliitani.midterm_itani.MainApp;

/**
 * Created by aliitani on 6/4/2017.
 */

public class Information {
    public String tickerSymbol;
    public int numberOfShares;
    public double pricePerShare;

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
