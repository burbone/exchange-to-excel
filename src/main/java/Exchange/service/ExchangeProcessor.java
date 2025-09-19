package Exchange.service;

import Exchange.api.ExchangeDataPrepare;
import Exchange.Data.BybitDataPrepare;
import Exchange.Data.KucoinDataPrepare;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeProcessor {
    
    public Map<String, ExchangeDataPrepare> processExchanges(List<String> exchanges, String symbol, String interval, String startTime, String endTime) {
        Map<String, ExchangeDataPrepare> preparedData = new HashMap<>();
        
        for (String exchange : exchanges) {
            ExchangeDataPrepare prepare = createPrepare(exchange);
            if (prepare != null) {
                prepare.setAll(symbol, interval, startTime, endTime);
                preparedData.put(exchange, prepare);
            }
        }
        
        return preparedData;
    }
    
    private ExchangeDataPrepare createPrepare(String exchange) {
        return switch (exchange.toLowerCase()) {
            case "bybit" -> new BybitDataPrepare();
            case "kucoin" -> new KucoinDataPrepare();
            default -> null;
        };
    }
} 