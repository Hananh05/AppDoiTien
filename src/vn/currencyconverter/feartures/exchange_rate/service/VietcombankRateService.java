package vn.currencyconverter.feartures.exchange_rate.service;

import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;
import vn.currencyconverter.feartures.exchange_rate.parser.ExchangeRateXmlParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VietcombankRateService {

    public List<ExchangeRate> getLatestRates() {
        try {
            // Đọc trực tiếp file XML mẫu trong máy để bypass hoàn toàn tường lửa ngân hàng
            String xmlData = Files.readString(Path.of("exrate.xml"));
            return ExchangeRateXmlParser.parse(xmlData);
            
        } catch (Exception e) {
            System.err.println("[Backend Error] Khong the doc file du lieu mau: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}