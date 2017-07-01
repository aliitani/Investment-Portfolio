package com.example.aliitani.Investment_Portfolio.MainApp;

/**
 * Created by aliitani on 6/4/2017.
 */

public class Information {
    private String tickerSymbol;
    private int numberOfShares;
    private double pricePerShare;
    private double totalPerShare;


    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTotalPerShare(double totalPerShare) {
        this.totalPerShare = totalPerShare;
    }

    public double getTotalPerShare() {
        return totalPerShare;
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
