package Exchange.api.KucoinApi;

import Exchange.api.ExchangeApi;
import Exchange.Data.KucoinDataPrepare;
import Exchange.model.Candle;
import Exchange.util.IntervalUtils;
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
public class KucoinKlineApi implements ExchangeApi {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KucoinDataPrepare dataPrepare = new KucoinDataPrepare();

    @Override
    public String getExchangeName() {
        return "kucoin";
    }

    @Override
    public List<Candle> getCandles(String symbol, String interval, long startTime, long endTime) {
        try {
            String formattedSymbol = dataPrepare.prepareSymbol(symbol);
            String kucoinInterval = IntervalUtils.toKucoinFormat(interval);
            long startTimeSeconds = startTime / 1000;
            long endTimeSeconds = endTime / 1000;

            String url = "https://api.kucoin.com/api/v1/market/candles" +
                    "?symbol=" + formattedSymbol +
                    "&type=" + kucoinInterval +
                    "&startAt=" + startTimeSeconds +
                    "&endAt=" + endTimeSeconds;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode dataNode = rootNode.get("data");

                if (dataNode != null && dataNode.isArray()) {
                    List<Candle> candles = new ArrayList<>();
                    for (JsonNode candleNode : dataNode) {
                        if (candleNode.isArray() && candleNode.size() >= 6) {
                            long timestamp = candleNode.get(0).asLong() * 1000;
                            double open = candleNode.get(1).asDouble();
                            double close = candleNode.get(2).asDouble();
                            double high = candleNode.get(3).asDouble();
                            double low = candleNode.get(4).asDouble();
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
