package vn.currencyconverter.core.network;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientProvider {
    private static HttpClient httpClient;

    private HttpClientProvider() {} 

    public static HttpClient getInstance() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder()
                    // CÚ CHỐT NẰM Ở ĐÂY: Bắt buộc ép về HTTP/1.1 để chơi với server cũ
                    .version(HttpClient.Version.HTTP_1_1) 
                    .connectTimeout(Duration.ofSeconds(15))
                    .build();
        }
        return httpClient;
    }
}