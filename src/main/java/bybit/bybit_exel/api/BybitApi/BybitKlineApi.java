package bybit.bybit_exel.api.BybitApi;

import bybit.bybit_exel.api.ExchangeApi;
import bybit.bybit_exel.Data.BybitDataPrepare;
import bybit.bybit_exel.model.Candle;
import bybit.bybit_exel.util.IntervalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class BybitKlineApi implements ExchangeApi {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BybitDataPrepare dataPrepare = new BybitDataPrepare();

    @Override
    public String getExchangeName() {
        return "bybit";
    }

    @Override
    public List<Candle> getCandles(String symbol, String interval, long startTime, long endTime) {
        try {
            String formattedSymbol = dataPrepare.prepareSymbol(symbol);
            String bybitInterval = IntervalUtils.toBybitFormat(interval);

            String url = "https://api.bybit.com/v5/market/kline" +
                    "?category=spot" +
                    "&symbol=" + formattedSymbol +
                    "&interval=" + bybitInterval +
                    "&start=" + startTime +
                    "&end=" + endTime +
                    "&limit=1000";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode resultNode = rootNode.get("result");
                JsonNode listNode = resultNode.get("list");

                if (listNode != null && listNode.isArray()) {
                    List<Candle> candles = new ArrayList<>();
                    for (JsonNode candleNode : listNode) {
                        if (candleNode.isArray() && candleNode.size() >= 6) {
                            long timestamp = candleNode.get(0).asLong();
                            double open = candleNode.get(1).asDouble();
                            double high = candleNode.get(2).asDouble();
                            double low = candleNode.get(3).asDouble();
                            double close = candleNode.get(4).asDouble();
                            double volume = candleNode.get(5).asDouble();

                            Candle candle = new Candle();
                            candle.setTimestamp(timestamp);
                            candle.setOpen(open);
                            candle.setClose(close);
                            candle.setHigh(high);
                            candle.setLow(low);
                            candle.setVolume(volume);

                            candles.add(candle);
                        }
                    }
                    return candles;
                }
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ArrayList<>();
    }
}
