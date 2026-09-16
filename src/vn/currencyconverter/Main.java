package vn.currencyconverter;

import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;
import vn.currencyconverter.feartures.exchange_rate.service.VietcombankRateService;
import vn.currencyconverter.feartures.converter.service.CurrencyConverterService;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("[SYSTEM] Dang khoi dong Backend ket noi Vietcombank...");

        // 1. Goi Service lay danh sach ti gia
        VietcombankRateService rateService = new VietcombankRateService();
        List<ExchangeRate> rates = rateService.getLatestRates();

        if (rates.isEmpty()) {
            System.out.println("[ERROR] Khong lay duoc du lieu. Hay kiem tra ket noi mang!");
            return;
        }

        System.out.println("[SUCCESS] Da keo thanh cong " + rates.size() + " ma tien te tu Vietcombank.");
        
        // 2. Tim ma USD trong danh sach vua lay ve
        ExchangeRate usdRate = null;
        for (ExchangeRate rate : rates) {
            if (rate.getCurrencyCode().equals("USD")) {
                usdRate = rate;
                break;
            }
        }

        // 3. Test Service quy doi tien
        if (usdRate != null) {
            System.out.println("[INFO] Ti gia USD hien tai - Mua: " + usdRate.getBuyRate() + " | Ban: " + usdRate.getSellRate());
            
            CurrencyConverterService converterService = new CurrencyConverterService();
            BigDecimal amountToConvert = new BigDecimal("100.00"); // Thu doi 100 USD
            
            // Gia su dung gia Ban (Sell Rate) de quy doi
            BigDecimal vndResult = converterService.convertToVnd(amountToConvert, usdRate.getSellRate());
            
            System.out.println("----------------------------------------");
            System.out.println("[RESULT] KET QUA QUY DOI: " + amountToConvert + " USD = " + vndResult + " VND");
            System.out.println("----------------------------------------");
        } else {
            System.out.println("[WARNING] Khong tim thay ma USD trong danh sach API tra ve.");
        }
    }
}