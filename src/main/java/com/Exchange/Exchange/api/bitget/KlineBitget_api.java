package com.Exchange.Exchange.api.bitget;

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

public class KlineBitget_api {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<Long, List<String>> klineBitget(HttpClient httpClient, String symbol,
                                              long startTime, long endTime,
                                              String interval) throws IOException, InterruptedException {

        Map<Long, List<String>> kline = new HashMap<>();

        String Interval = IntervalRemodelling.toBitgetFormat(interval);
        String Symbol = SymbolRemodeling.symbolRemodeling(symbol, "Bitget");

        String url = "https://api.bitget.com/api/v2/spot/market/candles" +
                "?symbol=" + Symbol +
                "&granularity=" + Interval +
                "&startTime=" + startTime +
                "&endTime=" + endTime +
                "&limit=1000";

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

                if (!"00000".equals(code.asText())) {
                    return kline;
                }

                JsonNode dataList = root.path("data");

                if (dataList.isArray() && !dataList.isEmpty()) {
                    for (JsonNode item : dataList) {
                        if (item.isArray() && item.size() >= 8) {
                            long timestamp = item.get(0).asLong();
                            String open = item.get(1).asText();
                            String high = item.get(2).asText();
                            String low = item.get(3).asText();
                            String close = item.get(4).asText();

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
