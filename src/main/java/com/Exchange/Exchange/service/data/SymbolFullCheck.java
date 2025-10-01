package com.Exchange.Exchange.service.data;

import com.Exchange.Exchange.api.OKX.SymbolOKX_api;
import com.Exchange.Exchange.api.bitget.SymbolBitget_api;
import com.Exchange.Exchange.api.bybit.SymbolBybit_api;
import com.Exchange.Exchange.api.kucoin.SymbolKucoin_api;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SymbolFullCheck {

    public boolean fullCheck(String exchange, String symbol) throws IOException, InterruptedException {
        symbol = SymbolRemodeling.symbolRemodeling(symbol, exchange);

        SymbolBybit_api symbolBybitApi = new SymbolBybit_api();
        SymbolKucoin_api symbolKucoinApi = new SymbolKucoin_api();
        SymbolBitget_api symbolBitgetApi = new SymbolBitget_api();
        SymbolOKX_api symbolOKXApi = new SymbolOKX_api();

        return switch (exchange) {
            case "Bybit" -> symbolBybitApi.symbolValid(symbol);
            case "Kucoin" -> symbolKucoinApi.symbolValid(symbol);
            case "Bitget" -> symbolBitgetApi.symbolValid(symbol);
            case "OKX" -> symbolOKXApi.symbolValid(symbol);
            default -> false;
        };
    }
}
