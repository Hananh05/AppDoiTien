package vn.currencyconverter.feartures.exchange_rate.model;

import java.math.BigDecimal;

public class ExchangeRate {
    private String currencyCode; 
    private String currencyName; 
    private BigDecimal buyRate;  
    private BigDecimal sellRate; 

    public ExchangeRate() {}

    public ExchangeRate(String currencyCode, String currencyName, BigDecimal buyRate, BigDecimal sellRate) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public String getCurrencyCode() { return currencyCode; }
    public String getCurrencyName() { return currencyName; }
    public BigDecimal getBuyRate() { return buyRate; }
    public BigDecimal getSellRate() { return sellRate; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }
    public void setBuyRate(BigDecimal buyRate) { this.buyRate = buyRate; }
    public void setSellRate(BigDecimal sellRate) { this.sellRate = sellRate; }
    
    @Override
    public String toString() {
        return "Mã: " + currencyCode + " | Mua: " + buyRate + " | Bán: " + sellRate;
    }
}