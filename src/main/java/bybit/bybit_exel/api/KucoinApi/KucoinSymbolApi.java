package bybit.bybit_exel.api.KucoinApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class KucoinSymbolApi {
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean checkSymbol(String symbol) {
        String url = "https://api.kucoin.com/api/v2/symbols/" + symbol.replace(" ", "-");
        try {
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            return "200000".equals(root.path("code").asText());
        } catch (Exception e) {
            return false;
        }
    }
}
