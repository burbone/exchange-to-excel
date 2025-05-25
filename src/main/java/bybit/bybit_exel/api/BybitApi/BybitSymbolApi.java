package bybit.bybit_exel.api.BybitApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class BybitSymbolApi {

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean checkSymbol(String symbol) {
        String url = "https://api-testnet.bybit.com/v5/market/instruments-info?category=spot&symbol=" + symbol.replace(" ", "");
        try {
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode list = root.path("result").path("list");
            return !list.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}