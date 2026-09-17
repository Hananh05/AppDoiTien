package vn.currencyconverter.features.history.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import vn.currencyconverter.features.exchange_rate.model.RateType;
import vn.currencyconverter.features.history.model.ConversionRecord;

public class ConversionHistoryService {

    private static final Path FILE_PATH =
        Paths.get("transaction_history.txt");

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Lưu giao dịch. Tầng giao diện sẽ xử lý lỗi IOException.
    public void saveRecord(ConversionRecord record) throws IOException {
        Objects.requireNonNull(record, "Giao dịch không được null");

        try (BufferedWriter writer = Files.newBufferedWriter(
                FILE_PATH,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(record.toCsvLine());
            writer.newLine();
        }
    }

    // Đọc lịch sử. File chưa tồn tại thì trả về danh sách rỗng.
    public List<ConversionRecord> getAllRecords() throws IOException {
        List<ConversionRecord> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                FILE_PATH, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                records.add(parseRecord(line, lineNumber));
            }

        } catch (NoSuchFileException e) {
            return records;
        }

        return records;
    }

    private ConversionRecord parseRecord(
            String line, int lineNumber
    ) throws IOException {

        String[] parts = line.split(",", -1);

        if (parts.length != 6) {
            throw new IOException(
                "Dòng " + lineNumber + " phải có đúng 6 trường dữ liệu."
            );
        }

        try {
            LocalDateTime time =
                LocalDateTime.parse(parts[0].trim(), FORMATTER);

            String code = parts[1].trim();

            if (code.isEmpty()) {
                throw new IllegalArgumentException("Mã tiền bị trống");
            }

            BigDecimal amount = new BigDecimal(parts[2].trim());
            BigDecimal rate = new BigDecimal(parts[3].trim());
            BigDecimal result = new BigDecimal(parts[4].trim());

            RateType type = RateType.valueOf(parts[5].trim());

            return new ConversionRecord(
                time, code, amount, rate, result, type
            );

        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new IOException(
                "Dữ liệu lịch sử không hợp lệ tại dòng " + lineNumber,
                e
            );
        }
    }
}