package Exchange.api.Bitget;

import Exchange.api.NowPricesApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class BitgetNowPricesApi implements NowPricesApi {
    
    public Map<String, String> getPrices() throws Exception {
        String url = "https://api.bitget.com/api/spot/v1/market/tickers";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> prices = new HashMap<>();
        JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
        
        String code = obj.get("code").getAsString();
        if (!"00000".equals(code)) {
            String msg = obj.has("msg") ? obj.get("msg").getAsString() : "Unknown error";
            throw new RuntimeException("Bitget API error: " + msg + " (code: " + code + ")");
        }
        
        JsonArray arr = obj.getAsJsonArray("data");

        for (JsonElement el : arr) {
            JsonObject o = el.getAsJsonObject();
            String symbol = o.get("symbol").getAsString();

            JsonElement priceEl = o.get("close");
            if (priceEl == null || priceEl.isJsonNull()) {
                priceEl = o.get("buyOne");
                if (priceEl == null || priceEl.isJsonNull()) {
                    continue;
                }
            }
            
            String price = priceEl.getAsString();
            prices.put(symbol, price);
        }

        return prices;
    }
}
