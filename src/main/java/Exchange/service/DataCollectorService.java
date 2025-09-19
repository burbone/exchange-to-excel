package Exchange.service;

import Exchange.api.ExchangeApi;
import Exchange.api.ExchangeDataPrepare;
import Exchange.model.Candle;
import Exchange.model.ColumnsInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataCollectorService {
    private final Map<String, ExchangeApi> exchangeApis;
    private final ParallelDataCollector parallelDataCollector;
    private final DataMerger dataMerger;

    public DataCollectorService(List<ExchangeApi> exchangeApis, 
                              ParallelDataCollector parallelDataCollector,
                              DataMerger dataMerger) {
        this.exchangeApis = exchangeApis.stream()
                .collect(Collectors.toMap(ExchangeApi::getExchangeName, api -> api));
        this.parallelDataCollector = parallelDataCollector;
        this.dataMerger = dataMerger;
    }

    public List<Candle> collectData(Map<String, ExchangeDataPrepare> preparedData, List<ColumnsInfo> columnsInfos, String userInterval) {
        Map<String, List<Candle>> exchangeData = parallelDataCollector.collectDataParallel(preparedData, userInterval);
        List<Candle> mergedCandles = dataMerger.mergeAndProcessData(exchangeData);
        dataMerger.validateData(mergedCandles);
        
        return mergedCandles;
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