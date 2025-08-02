package bybit.bybit_exel.api.BybitApi;

import bybit.bybit_exel.api.SymbolApi;
import bybit.bybit_exel.Data.BybitDataPrepare;
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
public class BybitSymbolApi implements SymbolApi {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BybitDataPrepare dataPrepare = new BybitDataPrepare();

    @Override
    public boolean isSymbolValid(String symbol) {
        try {
            String formattedSymbol = dataPrepare.prepareSymbol(symbol);
            String encodedSymbol = URLEncoder.encode(formattedSymbol, StandardCharsets.UTF_8);
            String url = "https://api.bybit.com/v5/market/instruments-info?category=spot&symbol=" + encodedSymbol;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode resultNode = rootNode.get("result");
                JsonNode listNode = resultNode.get("list");
                return listNode != null && listNode.isArray() && listNode.size() > 0;
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}