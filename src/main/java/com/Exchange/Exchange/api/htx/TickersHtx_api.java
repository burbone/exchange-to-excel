package com.Exchange.Exchange.api.htx;

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
public class TickersHtx_api {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Ticker> tickersHtx () {
        Map<String, Ticker> prices = new HashMap<>();

        String url = "https://api.htx.com/market/tickers";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Htx API вернул статус: {}, body: {}", response.statusCode(), response.body());
                return prices;
            }

            JsonNode root = objectMapper.readTree(response.body());


            JsonNode data = root.path("data");

            if (!data.isArray()) {
                log.warn("Неожиданная структура ответа от Htx API");
                return prices;
            }

            for (JsonNode item : data) {
                try {
                    String symbol = item.path("symbol").asText();
                    String bid1Price = item.path("bid").asText();
                    String ask1Price = item.path("ask").asText();

                    if (symbol.isEmpty() || bid1Price.isEmpty() || ask1Price.isEmpty()) {
                        continue;
                    }

                    Double bidPrice = Double.parseDouble(bid1Price);
                    Double askPrice = Double.parseDouble(ask1Price);

                    String normalizedSymbol = normalizeSymbol(symbol);

                    prices.put(normalizedSymbol, new Ticker(normalizedSymbol, bidPrice, askPrice));


                } catch (NumberFormatException e) {
                    log.debug("Не удалось парсить цену для символа {}: {}",
                            item.path("symbol").asText(), item.path("open").asText());
                } catch (Exception e) {
                    log.debug("Ошибка обработки элемента: {}", e.getMessage());
                }
            }
            log.debug("Получено {} цен с Htx", prices.size());

        } catch (IOException | InterruptedException e) {
            System.out.println("Htx api error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return prices;
    }

    private String normalizeSymbol(String symbol) {
        String normalized = symbol.toUpperCase().trim();
        normalized = normalized.replaceAll("-", "").replaceAll(" ", "");
        return normalized;
    }
}

