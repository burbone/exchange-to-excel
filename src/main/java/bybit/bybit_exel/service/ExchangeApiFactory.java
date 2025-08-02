package bybit.bybit_exel.service;

import bybit.bybit_exel.api.BybitApi.BybitNowPricesApi;
import bybit.bybit_exel.api.KucoinApi.KucoinNowPricesApi;
import bybit.bybit_exel.api.NowPricesApi;

public class ExchangeApiFactory {
    
    public NowPricesApi createApi(String exchangeName) {
        switch (exchangeName.toLowerCase()) {
            case "bybit":
                return new BybitNowPricesApi();
            case "kucoin":
                return new KucoinNowPricesApi();
            default:
                throw new IllegalArgumentException("Неподдерживаемая биржа: " + exchangeName);
        }
    }
} 