package vn.currencyconverter.feartures.history.service;

import vn.currencyconverter.feartures.history.model.ConversionRecord;
import vn.currencyconverter.feartures.exchange_rate.model.RateType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConversionHistoryService {

    // Nơi lưu trữ file lịch sử (Nằm ngay ngoài thư mục gốc của project)
    private static final String FILE_PATH = "transaction_history.txt";

    /**
     * Hàm lưu 1 giao dịch mới vào cuối file
     */
    public void saveRecord(ConversionRecord record) {
        // Tham số 'true' trong FileWriter nghĩa là ghi nối tiếp (append) vào cuối file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(record.toCsvLine());
            writer.newLine();
            System.out.println("[Backend] Da luu lich su giao dich xuong file.");
        } catch (IOException e) {
            System.err.println("[Backend Error] Khong the ghi file lich su: " + e.getMessage());
        }
    }

    /**
     * Hàm đọc toàn bộ lịch sử lên cho tầng UI hiển thị vào Bảng (Table)
     */
    public List<ConversionRecord> getAllRecords() {
        List<ConversionRecord> records = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Tách dữ liệu dựa vào dấu phẩy
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    LocalDateTime time = LocalDateTime.parse(parts[0], formatter);
                    String code = parts[1];
                    BigDecimal amount = new BigDecimal(parts[2]);
                    BigDecimal rate = new BigDecimal(parts[3]);
                    BigDecimal result = new BigDecimal(parts[4]);
                    RateType type = RateType.valueOf(parts[5]); // Chuyển chuỗi thành Enum

                    records.add(new ConversionRecord(time, code, amount, rate, result, type));
                }
            }
        } catch (IOException e) {
            // Lỗi này xảy ra khi file chưa tồn tại (chưa có ai đổi tiền lần nào) -> Bỏ qua
            System.out.println("[Backend] File lich su trong hoac chua duoc tao.");
        }

        return records;
    }
}