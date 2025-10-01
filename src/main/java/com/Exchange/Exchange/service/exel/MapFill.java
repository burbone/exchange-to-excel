package com.Exchange.Exchange.service.exel;

import com.Exchange.Exchange.dto.Request.Export_Request;
import com.Exchange.Exchange.service.exel.Connecter.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MapFill {
    @Getter
    Map<Long, List<String>> data = new ConcurrentHashMap<>();

    private Map<String, Object> exchangeConnectors = new HashMap<>();

    @PostConstruct
    public void init() {
        exchangeConnectors.put("bybit", bybitConnect);
        exchangeConnectors.put("kucoin", kucoinConnect);
        exchangeConnectors.put("bitget", bitgetConnect);
        exchangeConnectors.put("OKX", okxConnect);
        // добавить биржу
    }

    @Autowired private KlineBybitConnect bybitConnect;
    @Autowired private KlineKucoinConnect kucoinConnect;
    @Autowired private KlineBitgetConnect bitgetConnect;
    @Autowired private KlineOKXConnect okxConnect;
    // добавить биржу

    public void mapFill(String symbol, String interval, String timeStart, String timeEnd, List<Export_Request.ColumnInfo> columns) {
        clearAllData();
        data.clear();

        List<Long> timePoints = TimeRemodeling.timeSplitter(interval, timeStart, timeEnd);

        if (timePoints.isEmpty()) {
            return;
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Map.Entry<String, Object> entry : exchangeConnectors.entrySet()) {
            String exchangeName = entry.getKey();
            Object connector = entry.getValue();
            futures.add(CompletableFuture.runAsync(() -> {
                callGetAllData(connector, symbol, timeStart, timeEnd, interval, timePoints);
            }));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Map<String, Map<Long, List<String>>> allExchangeData = new HashMap<>();
        for (Map.Entry<String, Object> entry : exchangeConnectors.entrySet()) {
            String exchangeName = entry.getKey();
            Object connector = entry.getValue();
            Map<Long, List<String>> exchangeData = callGetData(connector);
            allExchangeData.put(exchangeName, exchangeData);
        }

        data = MapCreater.mapCreate(interval, timeStart, timeEnd);

        int columnsCount = columns.size();
        for (Long time : data.keySet()) {
            List<String> row = data.get(time);
            while (row.size() < columnsCount) {
                row.add("N/A");
            }
        }

        long intervalMs = IntervalRemodelling.getInterval(interval);
        long tolerance = Math.max(intervalMs * 2, 5 * 60 * 1000L);

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            Export_Request.ColumnInfo column = columns.get(columnIndex);
            String exchange = column.getExchange();
            String dataType = column.getType();

            Map<Long, List<String>> exchangeData = allExchangeData.get(exchange);
            if (exchangeData == null || exchangeData.isEmpty()) {
                continue;
            }

            for (Long time : data.keySet()) {
                String value = getValueFromExchangeData(time, dataType, exchangeData, tolerance);
                if (data.get(time).size() > columnIndex) {
                    data.get(time).set(columnIndex, value);
                }
            }
        }
    }

    private String getValueFromExchangeData(Long time, String dataType, Map<Long, List<String>> exchangeData, long tolerance) {
        List<String> timeData = exchangeData.get(time);

        if (timeData == null || timeData.isEmpty()) {
            Long closestTime = findClosestTime(time, exchangeData.keySet(), tolerance);
            if (closestTime != null) {
                timeData = exchangeData.get(closestTime);
            }
        }

        if (timeData == null || timeData.isEmpty()) return "N/A";

        int index = switch (dataType.toLowerCase()) {
            case "open" -> 0;
            case "high", "max" -> 1;
            case "low", "min" -> 2;
            case "close" -> 3;
            default -> -1;
        };

        if (index == -1 || timeData.size() <= index) {
            return "N/A";
        }

        String value = timeData.get(index);
        return value != null ? value : "N/A";
    }

    private Long findClosestTime(Long targetTime, Set<Long> availableTimes, long tolerance) {
        return availableTimes.stream()
                .filter(time -> Math.abs(time - targetTime) <= tolerance)
                .min(Comparator.comparingLong(time -> Math.abs(time - targetTime)))
                .orElse(null);
    }

    private void callGetAllData(Object connector, String symbol, String timeStart, String timeEnd, String interval, List<Long> timePoints) {
        try {
            if (connector instanceof KlineBybitConnect) {
                ((KlineBybitConnect) connector).BybitDataAll(symbol, timePoints, interval);
                //bybit
            } else if (connector instanceof KlineKucoinConnect) {
                ((KlineKucoinConnect) connector).KucoinDataAll(symbol, timePoints, interval);
                //kucoin
            } else if (connector instanceof KlineBitgetConnect) {
                ((KlineBitgetConnect) connector).BitgetDataAll(symbol, timePoints, interval);
                //bitget
            } else if (connector instanceof KlineOKXConnect) {
                ((KlineOKXConnect) connector).OKXDataAll(symbol, timePoints, interval);
                //OKX
            }
            // добавить новую биржу
        } catch (Exception e) {
        }
    }

    private Map<Long, List<String>> callGetData(Object connector) {
        try {
            if (connector instanceof KlineBybitConnect) {
                return ((KlineBybitConnect) connector).getBybitData();
                //bybit
            } else if (connector instanceof KlineKucoinConnect) {
                return ((KlineKucoinConnect) connector).getKucoinData();
                //kucoin
            } else if (connector instanceof KlineBitgetConnect) {
                return ((KlineBitgetConnect) connector).getBitgetData();
                //bitget
            } else if (connector instanceof KlineOKXConnect) {
                return ((KlineOKXConnect) connector).getOKXData();
                //OKX
            }
            //добавить новую биржу
        } catch (Exception e) {
        }
        return new HashMap<>();
    }

    private void clearAllData() {
        if(bybitConnect != null) {
            bybitConnect.clearBybit();
        }
        if(bitgetConnect != null) {
            bitgetConnect.clearBitget();
        }
        if(kucoinConnect != null) {
            kucoinConnect.clearKucoin();
        }
        if(okxConnect != null) {
            okxConnect.clearOKX();
        }
        // При добавлении новых бирж добавить их очистку здесь
    }
}