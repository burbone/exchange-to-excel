package bybit.bybit_exel.service;

import bybit.bybit_exel.api.BybitApi.BybitSymbolApi;
import bybit.bybit_exel.api.KucoinApi.KucoinSymbolApi;
import lombok.Data;

@Data
public class SymbolService {

    public boolean symbolService (String exchange, String symbol) {
        KucoinSymbolApi kucoinSymbolApi = new KucoinSymbolApi();
        BybitSymbolApi bybitSymbolApi = new BybitSymbolApi();

        return switch (exchange) {
            case "Kucoin" -> kucoinSymbolApi.checkSymbol(symbol);
            case "Bybit" -> bybitSymbolApi.checkSymbol(symbol);
            default -> false;
        };
    }
}
