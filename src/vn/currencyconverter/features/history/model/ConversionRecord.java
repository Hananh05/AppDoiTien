package vn.currencyconverter.feartures.history.model;

import vn.currencyconverter.feartures.exchange_rate.model.RateType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionRecord {
    private LocalDateTime timestamp; // Thời gian đổi tiền
    private String currencyCode;     // Mã tiền (VD: USD)
    private BigDecimal amount;       // Số tiền gốc cần đổi
    private BigDecimal rateApplied;  // Tỉ giá đã áp dụng
    private BigDecimal resultVnd;    // Kết quả ra VNĐ
    private RateType type;           // Loại giao dịch (BUY/SELL)

    public ConversionRecord(LocalDateTime timestamp, String currencyCode, BigDecimal amount, 
                            BigDecimal rateApplied, BigDecimal resultVnd, RateType type) {
        this.timestamp = timestamp;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.rateApplied = rateApplied;
        this.resultVnd = resultVnd;
        this.type = type;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCurrencyCode() { return currencyCode; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal rateApplied() { return rateApplied; }
    public BigDecimal getResultVnd() { return resultVnd; }
    public RateType getType() { return type; }

    // Chuyển object thành 1 dòng String phân cách bằng dấu phẩy để lưu xuống file .txt
    public String toCsvLine() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter) + "," + currencyCode + "," + amount + "," + rateApplied + "," + resultVnd + "," + type;
    }
}