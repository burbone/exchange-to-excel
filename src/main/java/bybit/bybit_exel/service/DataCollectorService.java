package bybit.bybit_exel.service;

import bybit.bybit_exel.api.ExchangeApi;
import bybit.bybit_exel.api.ExchangeDataPrepare;
import bybit.bybit_exel.model.Candle;
import bybit.bybit_exel.model.ColumnsInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataCollectorService {
    private final Map<String, ExchangeApi> exchangeApis;

    public DataCollectorService(List<ExchangeApi> exchangeApis) {
        this.exchangeApis = exchangeApis.stream()
                .collect(Collectors.toMap(ExchangeApi::getExchangeName, api -> api));
    }

    public List<Candle> collectData(Map<String, ExchangeDataPrepare> preparedData, List<ColumnsInfo> columnsInfos) {
        Map<String, List<Candle>> exchangeCandles = new HashMap<>();
        
        for (Map.Entry<String, ExchangeDataPrepare> entry : preparedData.entrySet()) {
            String exchange = entry.getKey();
            ExchangeDataPrepare prepare = entry.getValue();
            
            List<Candle> candles = getCandlesForExchange(exchange, prepare);
            candles.forEach(candle -> candle.setExchange(exchange));
            exchangeCandles.put(exchange, candles);
        }

        return exchangeCandles.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    
    private List<Candle> getCandlesForExchange(String exchange, ExchangeDataPrepare prepare) {
        ExchangeApi api = exchangeApis.get(exchange.toLowerCase());
        if (api == null) {
            return List.of();
        }
        
        return api.getCandles(
                prepare.getSymbol(), 
                prepare.getInterval(), 
                prepare.getStartTime(), 
                prepare.getEndTime()
        );
    }
}