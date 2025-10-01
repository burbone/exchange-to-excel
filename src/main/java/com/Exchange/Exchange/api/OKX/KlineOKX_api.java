package com.Exchange.Exchange.api.OKX;

import com.Exchange.Exchange.service.data.SymbolRemodeling;
import com.Exchange.Exchange.service.exel.IntervalRemodelling;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

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
public class KlineOKX_api {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<Long, List<String>> klineOKX(HttpClient httpClient, String symbol,
                                              long startTime, long endTime,
                                              String interval) throws IOException, InterruptedException {

        Map<Long, List<String>> kline = new HashMap<>();

        String okxInterval = IntervalRemodelling.toOKXFormat(interval);
        String okxSymbol = SymbolRemodeling.symbolRemodeling(symbol, "OKX");
        String url = "https://app.okx.com/api/v5/market/history-candles" +
                "?instId=" + okxSymbol +
                "&after=" + endTime +
                "&before=" + startTime +
                "&bar=" + okxInterval +
                "&limit=300";

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
                JsonNode retCode = root.path("code");

                if (retCode.asInt() != 0) {
                    return kline;
                }

                JsonNode result = root.path("data");

                if (result.isArray() && result.size() > 0) {
                    for (JsonNode item : result) {
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
