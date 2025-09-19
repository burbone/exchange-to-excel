package Exchange.service;

import Exchange.api.BybitApi.BybitSymbolApi;
import Exchange.api.KucoinApi.KucoinSymbolApi;
import lombok.Data;

@Data
public class SymbolService {

    public boolean symbolService(String exchange, String symbol) {
        KucoinSymbolApi kucoinSymbolApi = new KucoinSymbolApi();
        BybitSymbolApi bybitSymbolApi = new BybitSymbolApi();

        return switch (exchange) {
            case "Kucoin" -> kucoinSymbolApi.isSymbolValid(symbol);
            case "Bybit" -> bybitSymbolApi.isSymbolValid(symbol);
            default -> false;
        };
    }
}
