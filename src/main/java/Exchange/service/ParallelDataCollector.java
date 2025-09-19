package Exchange.service;

import Exchange.api.ExchangeApi;
import Exchange.api.ExchangeDataPrepare;
import Exchange.model.Candle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ParallelDataCollector {
    private final Map<String, ExchangeApi> exchangeApis;
    private final TimeRangeSplitter timeRangeSplitter;
    private static final int MAX_CONCURRENT_REQUESTS = 5;
    
    public ParallelDataCollector(List<ExchangeApi> exchangeApis, TimeRangeSplitter timeRangeSplitter) {
        this.exchangeApis = exchangeApis.stream()
                .collect(Collectors.toMap(ExchangeApi::getExchangeName, api -> api));
        this.timeRangeSplitter = timeRangeSplitter;
    }
    
    public Map<String, List<Candle>> collectDataParallel(
            Map<String, ExchangeDataPrepare> preparedData, 
            String userInterval) {
        if (preparedData == null || preparedData.isEmpty()) {
            return new HashMap<>();
        }
        
        Map<String, List<Candle>> allExchangeData = new HashMap<>();
        String firstExchange = preparedData.keySet().iterator().next();
        ExchangeDataPrepare firstPrepare = preparedData.get(firstExchange);
        List<TimeRangeSplitter.TimePart> timeParts = timeRangeSplitter.splitTimeRange(
                firstPrepare.getStartTime(), 
                firstPrepare.getEndTime(), 
                userInterval
        );
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < timeParts.size(); i += MAX_CONCURRENT_REQUESTS) {
                int endIndex = Math.min(i + MAX_CONCURRENT_REQUESTS, timeParts.size());
                List<TimeRangeSplitter.TimePart> batch = timeParts.subList(i, endIndex);
                
                List<CompletableFuture<Map<String, List<Candle>>>> batchFutures = batch.stream()
                    .map(timePart -> CompletableFuture.supplyAsync(() -> 
                        collectDataForTimePart(preparedData, timePart, executor), executor))
                    .collect(Collectors.toList());
                
                for (CompletableFuture<Map<String, List<Candle>>> future : batchFutures) {
                    try {
                        Map<String, List<Candle>> iterationData = future.get();
                        for (Map.Entry<String, List<Candle>> entry : iterationData.entrySet()) {
                            String exchange = entry.getKey();
                            List<Candle> candles = entry.getValue();
                            allExchangeData.computeIfAbsent(exchange, k -> new ArrayList<>())
                                    .addAll(candles);
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка при обработке временной части: " + e.getMessage());
                    }
                }
                
                if (endIndex < timeParts.size()) {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        return allExchangeData;
    }
    
    private Map<String, List<Candle>> collectDataForTimePart(
            Map<String, ExchangeDataPrepare> preparedData,
            TimeRangeSplitter.TimePart timePart,
            ExecutorService executor) {
        Map<String, CompletableFuture<List<Candle>>> futures = new HashMap<>();
        for (Map.Entry<String, ExchangeDataPrepare> entry : preparedData.entrySet()) {
            String exchange = entry.getKey();
            ExchangeDataPrepare prepare = entry.getValue();
            CompletableFuture<List<Candle>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    ExchangeApi api = exchangeApis.get(exchange.toLowerCase());
                    if (api == null) {
                        return new ArrayList<Candle>();
                    }
                    List<Candle> candles = api.getCandles(
                            prepare.getSymbol(),
                            prepare.getInterval(),
                            timePart.getStartTime(),
                            timePart.getEndTime()
                    );
                    candles.forEach(candle -> candle.setExchange(exchange));
                    return candles;
                } catch (Exception e) {
                    return new ArrayList<Candle>();
                }
            }, executor);
            futures.put(exchange, future);
        }
        Map<String, List<Candle>> result = new HashMap<>();
        for (Map.Entry<String, CompletableFuture<List<Candle>>> entry : futures.entrySet()) {
            try {
                result.put(entry.getKey(), entry.getValue().get());
            } catch (Exception e) {
                result.put(entry.getKey(), new ArrayList<>());
            }
        }
        return result;
    }
} 