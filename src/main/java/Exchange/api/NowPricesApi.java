package Exchange.api;

import java.util.Map;

public interface NowPricesApi {
    Map<String, String> getPrices() throws Exception;
}
