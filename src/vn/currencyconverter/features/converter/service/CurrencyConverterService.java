package vn.currencyconverter.feartures.converter.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverterService {

    public BigDecimal convertToVnd(BigDecimal amount, BigDecimal rate) {
        if (amount == null || rate == null || amount.signum() <= 0 || rate.signum() <= 0) {
            throw new IllegalArgumentException("Số tiền hoặc tỉ giá không hợp lệ.");
        }
        
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}