package com.Exchange.Exchange.service.data;

import org.springframework.stereotype.Service;

@Service
public class SymbolRemodeling {

    public static String symbolRemodeling(String symbol, String exchange) {
        return switch (exchange) {
            case "Bybit" -> symbol.toUpperCase().replaceAll(" ", "");
            case "Kucoin" -> symbol.toUpperCase().trim().replaceAll(" ", "-");
            case "Bitget" -> symbol.toUpperCase().replaceAll(" ", "");
            case "OKX" -> symbol.toUpperCase().replaceAll(" ", "-");
            default -> "";
        };
    }
}
