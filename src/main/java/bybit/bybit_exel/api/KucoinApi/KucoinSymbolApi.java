package bybit.bybit_exel.api.KucoinApi;

import bybit.bybit_exel.api.SymbolApi;
import bybit.bybit_exel.Data.KucoinDataPrepare;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class KucoinSymbolApi implements SymbolApi {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KucoinDataPrepare dataPrepare = new KucoinDataPrepare();

    @Override
    public boolean isSymbolValid(String symbol) {
        try {
            String formattedSymbol = dataPrepare.prepareSymbol(symbol);
            String encodedSymbol = URLEncoder.encode(formattedSymbol, StandardCharsets.UTF_8);
            String url = "https://api.kucoin.com/api/v1/symbols/" + encodedSymbol;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode dataNode = rootNode.get("data");
                return dataNode != null && dataNode.has("symbol");
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
