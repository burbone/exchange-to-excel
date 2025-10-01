package com.Exchange.Exchange.api.OKX;

import com.Exchange.Exchange.dto.CustomCollections.Ticker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TickersOKX_api {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Ticker> tickersOKX() {
        Map<String, Ticker> prices = new HashMap<>();

        String url = "https://app.okx.com/api/v5/market/tickers?instType=SPOT";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("OKX API вернул статус: {}, body: {}", response.statusCode(), response.body());
                return prices;
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode retCode = root.path("code");

            if (retCode.asInt() != 0) {
                log.warn("OKX API ошибка: retCode={}, retMsg={}", retCode.asInt(), root.path("retMsg").asText());
                return prices;
            }

            JsonNode data = root.path("data");

            if (!data.isArray()) {
                log.warn("Неожиданная структура ответа от OKX API");
                return prices;
            }

            for (JsonNode item : data) {
                try {
                    String symbol = item.path("instId").asText();
                    String bid1Price = item.path("bidPx").asText();
                    String ask1Price = item.path("askPx").asText();

                    if (symbol.isEmpty() || bid1Price.isEmpty() || ask1Price.isEmpty()) {
                        continue;
                    }

                    Double bidPrice = Double.parseDouble(bid1Price);
                    Double askPrice = Double.parseDouble(ask1Price);

                    String normalizedSymbol = normalizeSymbol(symbol);

                    prices.put(normalizedSymbol, new Ticker(normalizedSymbol, bidPrice, askPrice));

                } catch (NumberFormatException e) {
                    log.debug("Не удалось парсить цену для символа {}: {}",
                            item.path("symbol").asText(), item.path("lastPrice").asText());
                } catch (Exception e) {
                    log.debug("Ошибка обработки элемента: {}", e.getMessage());
                }
            }
            log.debug("Получено {} цен с OKX", prices.size());

        } catch (IOException | InterruptedException e) {
            System.out.println("OKX api error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return prices;
    }

    private String normalizeSymbol(String symbol) {
        String normalized = symbol.toUpperCase().trim();
        normalized = normalized.replaceAll("-", "");
        return normalized;
    }
}

