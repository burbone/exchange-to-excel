package Exchange.service;

import Exchange.model.Candle;
import Exchange.model.ColumnsInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataProcessingService {
    
    public List<Long> extractTimestamps(List<Candle> candles, List<ColumnsInfo> columnsInfo) {
        Set<String> exchanges = columnsInfo.stream()
                .map(ColumnsInfo::getExchange)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return candles.stream()
                .filter(c -> exchanges.contains(c.getExchange().toLowerCase()))
                .map(Candle::getTimestamp)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public Map<Long, Map<String, Double>> createDataMap(List<Candle> candles, List<ColumnsInfo> columnsInfo) {
        List<Long> times = extractTimestamps(candles, columnsInfo);
        Map<Long, Map<String, Double>> dataMap = new HashMap<>();
        
        for (Long time : times) {
            Map<String, Double> row = new HashMap<>();
            
            for (ColumnsInfo col : columnsInfo) {
                String key = col.getExchange() + ":" + col.getDataType();
                
                Candle candle = candles.stream()
                    .filter(c -> c.getTimestamp() == time)
                    .filter(c -> col.getExchange().equalsIgnoreCase(c.getExchange()))
                    .findFirst()
                    .orElse(null);
                    
                if (candle != null) {
                    Double value = extractValue(candle, col.getDataType());
                    row.put(key, value);
                } else {
                    row.put(key, null);
                }
            }
            
            dataMap.put(time, row);
        }
        
        return dataMap;
    }
    
    private Double extractValue(Candle candle, String dataType) {
        return switch (dataType.toLowerCase()) {
            case "open" -> candle.getOpen();
            case "close" -> candle.getClose();
            case "high" -> candle.getHigh();
            case "low" -> candle.getLow();
            default -> null;
        };
    }
} 