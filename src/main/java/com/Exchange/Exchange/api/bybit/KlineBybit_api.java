package com.Exchange.Exchange.api.bybit;

import com.Exchange.Exchange.service.data.SymbolRemodeling;
import com.Exchange.Exchange.service.exel.IntervalRemodelling;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
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

@Component
public class KlineBybit_api {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<Long, List<String>> klineBybit(HttpClient httpClient, String symbol,
                                              long startTime, long endTime,
                                              String interval) throws IOException, InterruptedException {

        Map<Long, List<String>> kline = new HashMap<>();

        String bybitInterval = IntervalRemodelling.toBybitFormat(interval);
        String bybitSymbol = SymbolRemodeling.symbolRemodeling(symbol, "Bybit");
        String url = "https://api.bybit.com/v5/market/kline" +
                "?category=spot" +
                "&symbol=" + bybitSymbol +
                "&interval=" + bybitInterval +
                "&start=" + startTime +
                "&end=" + endTime +
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
                JsonNode retCode = root.path("retCode");

                if (retCode.asInt() != 0) {
                    return kline;
                }

                JsonNode result = root.path("result");
                JsonNode dataList = result.path("list");

                if (dataList.isArray() && dataList.size() > 0) {
                    for (JsonNode item : dataList) {
                        if (item.isArray() && item.size() >= 5) {
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