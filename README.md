# NHÓM 4 – ỨNG DỤNG QUY ĐỔI TIỀN TỆ

Ứng dụng desktop viết bằng Java Swing, quy đổi USD, JPY, GBP, CAD, AUD và SGD sang VND. Giao diện tông hồng gồm ba màn hình: Quy đổi, Bảng tỷ giá và Lịch sử.

## Chức năng

- Tải tỷ giá trực tuyến từ Vietcombank khi mở ứng dụng.
- Cập nhật lại tỷ giá theo yêu cầu và hiển thị thời điểm nguồn công bố.
- Chọn giá ngân hàng mua tiền mặt hoặc mua chuyển khoản khi bán ngoại tệ lấy VND.
- Tính toán bằng `BigDecimal`, kiểm tra số tiền lớn hơn 0.
- Lưu và đọc lịch sử từ `transaction_history.txt`.
- Tải dữ liệu bằng `SwingWorker` để tránh làm đứng giao diện.

## Yêu cầu

- JDK 17 trở lên; bản hiện tại đã được kiểm tra bằng JDK 22 trên Windows.
- Kết nối Internet để tải tỷ giá.
- Có thể dùng VS Code, IntelliJ IDEA hoặc terminal. Không cần Maven hay thư viện ngoài.

## Chạy trên Windows bằng PowerShell

Mở terminal tại thư mục gốc dự án, nơi chứa `src`, rồi chạy:

```powershell
$compiler = (Get-Command javac.exe).Source
$runtime = Join-Path (Split-Path $compiler) 'java.exe'
$files = Get-ChildItem .\src -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName
New-Item -ItemType Directory -Force -Path out | Out-Null
& $compiler -encoding UTF-8 -d out $files
if ($LASTEXITCODE -eq 0) {
    & $runtime -cp out vn.currencyconverter.Main
}
```

Lệnh chọn `java.exe` cùng thư mục với `javac.exe` để tránh biên dịch bằng Java mới nhưng chạy bằng Java 8. Sau khi sửa code, cần biên dịch lại. Đóng cửa sổ ứng dụng cũ trước khi chạy bản mới.

Trong IDE, đặt `src` làm source root và chạy `vn.currencyconverter.Main`.

## Cách sử dụng

1. Mở ứng dụng và chờ tải tỷ giá thành công.
2. Nhập số tiền, ví dụ `100` hoặc `100,50`. Không nhập dấu phân cách hàng nghìn.
3. Chọn ngoại tệ và hình thức tiền mặt/chuyển khoản.
4. Bấm **Quy đổi sang VND** hoặc Enter trong ô số tiền.
5. Mở **Lịch sử** để xem giao dịch đã lưu.
6. Mở **Bảng tỷ giá → Cập nhật trực tuyến** để lấy lại dữ liệu.

## Nguồn dữ liệu và cách tính

- Trang tỷ giá: https://www.vietcombank.com.vn/vi-VN/KHCN/Cong-cu-tien-ich/Ty-gia
- Endpoint JSON đang được trang sử dụng: https://www.vietcombank.com.vn/api/exchangerates?date=
- Thời điểm công bố đọc từ `UpdatedDate`; dữ liệu ngoại tệ nằm trong `Data`.
- Các trường `cash`, `transfer`, `sell` lần lượt là mua tiền mặt, mua chuyển khoản và bán.

```text
VND = Số ngoại tệ × Tỷ giá mua theo hình thức đã chọn
```

Ứng dụng quy đổi ngoại tệ sang VND nên không dùng giá ngân hàng bán để tính số VND nhận được. Cột bán vẫn được hiển thị để tham khảo. Kết quả làm tròn đến 2 chữ số thập phân, chưa bao gồm phí giao dịch.

## Cấu trúc mã nguồn

```text
src/vn/currencyconverter/
├── Main.java
├── core/
│   ├── constants/       # URL nguồn dữ liệu
│   ├── network/         # HttpClient dùng chung
│   └── theme/           # Màu sắc và kiểu giao diện
└── feartures/
    ├── home/ui/         # Cửa sổ chính và điều hướng
    ├── converter/       # Giao diện, nghiệp vụ quy đổi
    ├── exchange_rate/   # Model, parser JSON/XML, service, bảng tỷ giá
    └── history/         # Model, đọc/ghi file, giao diện lịch sử
```

Tên `feartures` được giữ theo dự án hiện tại. Nếu đổi thành `features`, phải đổi đồng bộ thư mục, package và import.

## Lưu trữ và giới hạn

- `transaction_history.txt` được tạo trong thư mục chạy, không nằm bên trong `src`.
- Lịch sử lưu thời điểm quy đổi, mã tiền, số lượng, tỷ giá áp dụng, kết quả và loại tỷ giá.
- Các giao dịch thử bằng dữ liệu mẫu trước đây vẫn có thể còn trong lịch sử; định dạng hiện tại chưa lưu nguồn dữ liệu của từng giao dịch.
- File `exrate.xml` cũ không còn được dùng làm nguồn tỷ giá tự động. Không coi CAD/SGD giả định trong file đó là báo giá ngân hàng.
- Khi tải thất bại, ứng dụng báo lỗi và khóa quy đổi; không tự thay bằng số liệu mẫu.
- Endpoint phụ thuộc website Vietcombank và có thể thay đổi; đây không phải cam kết về API công khai ổn định.
- Ứng dụng phục vụ học tập, tham khảo; không thực hiện giao dịch tiền thật.

## Xử lý lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| `UnsupportedClassVersionError` | Dùng bộ lệnh trên để chạy Java cùng bộ với trình biên dịch |
| `javac` không được nhận diện | Cài JDK và thêm thư mục `bin` vào PATH |
| Không tải được tỷ giá | Kiểm tra mạng, thử trang Vietcombank rồi bấm cập nhật lại |
| Không tìm thấy class | Chạy tại thư mục gốc, biên dịch toàn bộ `src`, kiểm tra package/import |
| Không lưu được lịch sử | Kiểm tra quyền ghi vào thư mục chạy |

## Thành viên Nhóm 4

| Họ tên | Mã sinh viên | Công việc |
|---|---|---|
| Nguyễn Tùng Dương | 2823230371 | |
| Nguyễn Anh Hán | 2823156551 | |
| Ma Thi Quỳnh Lan | 2823230454 | |

## Kiểm tra bản sửa

Đã biên dịch bằng JDK 22, kiểm tra đọc JSON, xử lý giá mua tiền mặt không có và phản hồi sai định dạng. Kiểm tra trực tuyến ngày 16/09/2026 nhận 20 ngoại tệ từ Vietcombank, gồm đủ 6 mã ứng dụng hỗ trợ. Trạng thái mạng và tỷ giá có thể thay đổi ở các lần chạy tiếp theo.
