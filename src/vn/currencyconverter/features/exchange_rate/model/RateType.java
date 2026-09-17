package vn.currencyconverter.features.exchange_rate.model;

/**
 * Enum quản lý trạng thái giao dịch để tránh nhầm lẫn cột giá.
 * Thầy cô hỏi thì bảo: "Dùng Enum để fix cứng dữ liệu đầu vào, an toàn hơn dùng String dễ gõ sai".
 */
public enum RateType {
    TRANSFER_BUY,
    BUY,  // Khách bán ngoại tệ, Ngân hàng MUA vào
    SELL  // Khách mua ngoại tệ, Ngân hàng BÁN ra
}