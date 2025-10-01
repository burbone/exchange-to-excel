package com.Exchange.Exchange.api.bitget;

import com.Exchange.Exchange.service.data.SymbolRemodeling;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class SymbolBitget_api {
    private final SymbolRemodeling symbolRemodeling = new SymbolRemodeling();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean symbolValid(String symbol) throws IOException, InterruptedException {
        String url = "https://api.bitget.com/api/v2/spot/public/symbols?symbol=" + symbol;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode rootNode = objectMapper.readTree(response.body());
            return "00000".equals(rootNode.get("code").asText());
        }
        return false;
    }
}
