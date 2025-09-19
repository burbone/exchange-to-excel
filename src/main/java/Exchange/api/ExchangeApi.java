package Exchange.api;

import Exchange.model.Candle;
import java.util.List;

public interface ExchangeApi {
    List<Candle> getCandles(String symbol, String interval, long startTime, long endTime);
    String getExchangeName();
} 