package Exchange.service;

import Exchange.api.BybitApi.BybitNowPricesApi;
import Exchange.api.KucoinApi.KucoinNowPricesApi;
import Exchange.api.Bitget.BitgetNowPricesApi;
import Exchange.api.NowPricesApi;

public class ExchangeApiFactory {
    
    public NowPricesApi createApi(String exchangeName) {
        switch (exchangeName.toLowerCase()) {
            case "bybit":
                return new BybitNowPricesApi();
            case "kucoin":
                return new KucoinNowPricesApi();
            case "bitget":
                return new BitgetNowPricesApi();
            default:
                throw new IllegalArgumentException("Неподдерживаемая биржа: " + exchangeName);
        }
    }
} 