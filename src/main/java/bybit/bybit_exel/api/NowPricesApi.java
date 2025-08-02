package bybit.bybit_exel.api;

import java.util.Map;

public interface NowPricesApi {
    Map<String, String> getPrices() throws Exception;
}
