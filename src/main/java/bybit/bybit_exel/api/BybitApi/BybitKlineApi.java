package bybit.bybit_exel.api.BybitApi;

import bybit.bybit_exel.api.ExchangeApi;
import bybit.bybit_exel.model.Candle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class BybitKlineApi implements ExchangeApi {
    private final RestTemplate restTemplate;

    public BybitKlineApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Candle> getCandles(String symbol, String interval, long startTime, long endTime) {
        try {
            String url = "https://api.bybit.com/v5/market/kline" +
                    "?category=spot" +
                    "&symbol=" + symbol +
                    "&interval=" + interval +
                    "&start=" + startTime +
                    "&end=" + endTime;

            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode list = root.path("result").path("list");

            List<Candle> candles = new ArrayList<>();

            for (JsonNode candle : list) {
                long timestamp = Long.parseLong(candle.get(0).asText());
                double open = Double.parseDouble(candle.get(1).asText());
                double high = Double.parseDouble(candle.get(2).asText());
                double low = Double.parseDouble(candle.get(3).asText());
                double close = Double.parseDouble(candle.get(4).asText());
                
                Candle candleData = new Candle(timestamp, open, high, low, close);
                candles.add(candleData);
            }

            return candles;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public String getExchangeName() {
        return "bybit";
    }
}
