package com.Exchange.Exchange.api.kucoin;

import com.Exchange.Exchange.service.data.SymbolRemodeling;
import com.Exchange.Exchange.service.exel.IntervalRemodelling;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlineKucoin_api {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<Long, List<String>> klineKucoin(HttpClient httpClient, String symbol,
                                              long startTime, long endTime,
                                              String interval) throws IOException, InterruptedException {

        Map<Long, List<String>> kline = new HashMap<>();

        String Interval = IntervalRemodelling.toKucoinFormat(interval);
        String Symbol = SymbolRemodeling.symbolRemodeling(symbol, "Kucoin");

        String url = "https://api.kucoin.com/api/v1/market/candles" +
                "?symbol=" + Symbol +
                "&type=" + Interval +
                "&startAt=" + startTime / 1000 +
                "&endAt=" + endTime / 1000;

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode code = root.path("code");

                if (!"200000".equals(code.asText())) {
                    return kline;
                }

                JsonNode dataList = root.path("data");

                if (dataList.isArray() && !dataList.isEmpty()) {
                    for (JsonNode item : dataList) {
                        if (item.isArray() && item.size() >= 7) {
                            long timestamp = item.get(0).asLong() * 1000;
                            String open = item.get(1).asText();
                            String close = item.get(2).asText();
                            String high = item.get(3).asText();
                            String low = item.get(4).asText();

                            List<String> values = new ArrayList<>();
                            values.add(open);
                            values.add(high);
                            values.add(low);
                            values.add(close);

                            kline.put(timestamp, values);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return kline;
    }
}
