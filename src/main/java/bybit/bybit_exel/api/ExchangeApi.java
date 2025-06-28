package bybit.bybit_exel.api;

import bybit.bybit_exel.model.Candle;
import java.util.List;

public interface ExchangeApi {
    List<Candle> getCandles(String symbol, String interval, long startTime, long endTime);
    String getExchangeName();
} 