package bybit.bybit_exel.api.KucoinApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class KucoinSymbolApi {

    public boolean checkSymbol(String symbol) {
        try {
            String formattedSymbol = symbol.replace(" ", "-");
            String url = "https://api.kucoin.com/api/v1/symbols/" + formattedSymbol;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("code").asText().equals("200000");
        } catch (Exception e) {
            return false;
        }
    }
}
