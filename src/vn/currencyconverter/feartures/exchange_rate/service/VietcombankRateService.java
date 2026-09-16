package vn.currencyconverter.feartures.exchange_rate.service;
import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;
import vn.currencyconverter.feartures.exchange_rate.parser.ExchangeRateXmlParser;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
public class VietcombankRateService {
    public List<ExchangeRate> getLatestRates() {
        try { return ExchangeRateXmlParser.parse(Files.readString(Path.of("exrate.xml"))); }
        catch (IOException e) { throw new IllegalStateException("Không đọc được exrate.xml trong thư mục chạy: " + Path.of("").toAbsolutePath(), e); }
    }
}
