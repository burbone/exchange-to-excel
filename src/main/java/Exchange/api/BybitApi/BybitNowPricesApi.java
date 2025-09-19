package Exchange.api.BybitApi;

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

public class BybitNowPricesApi implements NowPricesApi {
    public Map<String, String> getPrices() throws Exception {
        String url = "https://api.bybit.com/v5/market/tickers?category=spot";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> prices = new HashMap<>();
        JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray arr = obj.getAsJsonObject("result").getAsJsonArray("list");

        for (JsonElement el : arr) {
            JsonObject o = el.getAsJsonObject();
            String symbol = o.get("symbol").getAsString();
            String price = o.get("lastPrice").getAsString();
            prices.put(symbol, price);
        }

        return prices;
    }
}
