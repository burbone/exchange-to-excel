package bybit.bybit_exel.api.BybitApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class BybitSymbolApi {

    public boolean checkSymbol(String symbol) {
        try {
            String formattedSymbol = symbol.replace(" ", "");
            String url = "https://api.bybit.com/v5/market/instruments-info?category=spot&symbol=" + formattedSymbol;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode result = root.path("result");
            JsonNode list = result.path("list");

            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}