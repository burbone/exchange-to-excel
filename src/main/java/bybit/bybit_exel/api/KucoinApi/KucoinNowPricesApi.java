package bybit.bybit_exel.api.KucoinApi;

import bybit.bybit_exel.api.NowPricesApi;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.*;

public class KucoinNowPricesApi implements NowPricesApi {
    public Map<String, String> getPrices() throws Exception {
        String url = "https://api.kucoin.com/api/v1/market/allTickers";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> prices = new HashMap<>();
        JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray arr = obj.getAsJsonObject("data").getAsJsonArray("ticker");

        for (JsonElement el : arr) {
            JsonObject o = el.getAsJsonObject();
            String symbol = o.get("symbol").getAsString().replace("-", "");
            JsonElement priceEl = o.get("last");
            if (priceEl == null || priceEl.isJsonNull()) continue;
            String price = priceEl.getAsString();
            prices.put(symbol, price);
        }

        return prices;
    }
}
