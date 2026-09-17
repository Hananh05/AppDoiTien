package vn.currencyconverter.feartures.exchange_rate.service;
import vn.currencyconverter.core.constants.AppConstants;
import vn.currencyconverter.core.network.HttpClientProvider;
import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;
import vn.currencyconverter.feartures.exchange_rate.parser.ExchangeRateJsonParser;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.List;
import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
public class VietcombankRateService {
    private String publishedAt = "Chưa tải dữ liệu";
    public String getPublishedAt() { return publishedAt; }
    public List<ExchangeRate> getLatestRates() {
        try {
            var request = HttpRequest.newBuilder(URI.create(AppConstants.VCB_API_URL))
                .timeout(Duration.ofSeconds(25))
                .header("Accept", "application/json")
                .GET().build();
            var response = HttpClientProvider.getInstance().send(request, HttpResponse.BodyHandlers.ofString(java.nio.charset.StandardCharsets.UTF_8));
            if (response.statusCode()!=200) throw new IOException("Vietcombank trả HTTP " + response.statusCode());
            var result = ExchangeRateJsonParser.parse(response.body());
            publishedAt = result.publishedAt();
            return result.rates();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Đã hủy tải tỷ giá.", e);
        } catch (HttpTimeoutException e) {
            throw new IllegalStateException("Kết nối Vietcombank quá thời gian chờ. Kiểm tra mạng rồi bấm Cập nhật trực tuyến. Không dùng dữ liệu mẫu.", e);
        } catch (Exception e) {
            throw new IllegalStateException("Không lấy được tỷ giá trực tuyến: " + e.getMessage() + ". Không dùng dữ liệu mẫu.", e);
        }
    }
}
