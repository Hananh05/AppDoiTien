package vn.currencyconverter.core.network;
import java.net.http.HttpClient;
import java.time.Duration;
public final class HttpClientProvider {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .followRedirects(HttpClient.Redirect.NORMAL).build();
    private HttpClientProvider() {}
    public static HttpClient getInstance() { return CLIENT; }
}
