package Exchange.service;

import Exchange.model.Candle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataMerger {
    public List<Candle> mergeAndProcessData(Map<String, List<Candle>> exchangeData) {
        List<Candle> allCandles = new ArrayList<>();
        for (Map.Entry<String, List<Candle>> entry : exchangeData.entrySet()) {
            List<Candle> candles = entry.getValue();
            allCandles.addAll(candles);
        }
        List<Candle> uniqueCandles = removeDuplicates(allCandles);
        uniqueCandles.sort(Comparator.comparingLong(Candle::getTimestamp));
        return uniqueCandles;
    }
    
    private List<Candle> removeDuplicates(List<Candle> candles) {
        return candles.stream()
                .collect(Collectors.toMap(
                    candle -> candle.getTimestamp() + "_" + candle.getExchange(),
                    candle -> candle,
                    (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
    
    public void validateData(List<Candle> candles) {
        if (candles.isEmpty()) {
            return;
        }
        checkForGaps(candles);
    }
    
    private void checkForGaps(List<Candle> candles) {
        if (candles.size() < 2) return;
        Map<String, List<Candle>> byExchange = candles.stream()
                .collect(Collectors.groupingBy(Candle::getExchange));
        for (Map.Entry<String, List<Candle>> entry : byExchange.entrySet()) {
            List<Candle> exchangeCandles = entry.getValue();
            exchangeCandles.sort(Comparator.comparingLong(Candle::getTimestamp));
            long expectedInterval = 0;
            if (exchangeCandles.size() >= 2) {
                expectedInterval = exchangeCandles.get(1).getTimestamp() - exchangeCandles.get(0).getTimestamp();
            }
            int gaps = 0;
            for (int i = 1; i < exchangeCandles.size(); i++) {
                long actualInterval = exchangeCandles.get(i).getTimestamp() - exchangeCandles.get(i-1).getTimestamp();
                if (actualInterval > expectedInterval * 1.5) {
                    gaps++;
                }
            }
        }
    }
} 