package com.Exchange.Exchange.service.tickers;

import com.Exchange.Exchange.api.OKX.TickersOKX_api;
import com.Exchange.Exchange.api.bitget.TickersBitget_api;
import com.Exchange.Exchange.api.bybit.TickersBybit_api;
import com.Exchange.Exchange.api.htx.TickersHtx_api;
import com.Exchange.Exchange.api.kucoin.TickersKucoin_api;
import com.Exchange.Exchange.dto.CustomCollections.Ticker;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
@RequiredArgsConstructor
@Data
public class PriceRepo {

    private final TickersBybit_api tickersBybitApi;
    private final TickersBitget_api tickersBitgetApi;
    private final TickersKucoin_api tickersKucoinApi;
    private final TickersOKX_api tickersOKXApi;
    private final TickersHtx_api tickersHtxApi;
    //добавить сюда новую биржу

    private final Map<String, List<Ticker>> cachedPrices = new ConcurrentHashMap<>();
    private LocalDateTime lastCacheUpdate;
    private boolean initialized = false;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            try {
                updateCachedData();
                initialized = true;
                log.info("PriceRepo успешно инициализирован");
            } catch (Exception e) {
                log.error("Ошибка инициализации PriceRepo: {}", e.getMessage());
            }
        }).start();
    }

    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void updateCachedData() {
        if (!initialized) {
            log.debug("PriceRepo еще не инициализирован, пропускаем обновление");
            return;
        }

        log.debug("Обновление кэшированных данных...");
        try {
            Map<String, List<Ticker>> newData = getAllCommonPairs();
            if (!newData.isEmpty()) {
                cachedPrices.clear();
                cachedPrices.putAll(newData);
                lastCacheUpdate = LocalDateTime.now();
                log.debug("Кэш обновлен успешно. Количество символов: {}", cachedPrices.size());
            } else {
                log.warn("Получены пустые данные, кэш не обновлен");
            }
        } catch (Exception e) {
            log.error("Ошибка при обновлении кэша: {}", e.getMessage());
        }
    }

    public Map<String, List<Ticker>> getCachedData() {
        return new HashMap<>(cachedPrices);
    }

    public Map<String, List<Ticker>> getAllCommonPairs () {
        Map<String, List<Ticker>> result = new HashMap<>();

        try {

            if (tickersBybitApi == null
                    || tickersBitgetApi == null
                    || tickersKucoinApi == null
                    || tickersOKXApi == null
                    || tickersHtxApi == null)
                    //добавить биржу
            {
                log.warn("API клиенты еще не инициализированы");
                return result;
            }

            Map<String, Ticker> bybitPrices = tickersBybitApi.tickersBybit();
            Map<String, Ticker> kucoinPrices = tickersKucoinApi.tickersKucoin();
            Map<String, Ticker> bitgetPrices = tickersBitgetApi.tickersBitget();
            Map<String, Ticker> okxPrices = tickersOKXApi.tickersOKX();
            Map<String, Ticker> htxPrices = tickersHtxApi.tickersHtx();
            //сюда добавить новую биржу

            Set<String> commonSymbols = new HashSet<>(bitgetPrices.keySet());
            commonSymbols.retainAll(bybitPrices.keySet());
            commonSymbols.retainAll(kucoinPrices.keySet());
            commonSymbols.retainAll(okxPrices.keySet());
            commonSymbols.retainAll(htxPrices.keySet());
            //добавить биржу

            for (String symbol : commonSymbols) {
                List<Ticker> tickers = new ArrayList<>();

                Ticker bybitTicker = bybitPrices.get(symbol);
                tickers.add(new Ticker("Bybit", bybitTicker.getBidPrice(), bybitTicker.getAskPrice()));

                Ticker bitgetTicker = bitgetPrices.get(symbol);
                tickers.add(new Ticker("Bitget", bitgetTicker.getBidPrice(), bitgetTicker.getAskPrice()));

                Ticker kucoinTicker = kucoinPrices.get(symbol);
                tickers.add(new Ticker("Kucoin", kucoinTicker.getBidPrice(), kucoinTicker.getAskPrice()));

                Ticker okxTicker = okxPrices.get(symbol);
                tickers.add(new Ticker("OKX", okxTicker.getBidPrice(), okxTicker.getAskPrice()));

                Ticker htxTicker = htxPrices.get(symbol);
                tickers.add(new Ticker("HTX", htxTicker.getBidPrice(), htxTicker.getAskPrice()));

                //сюда добавить новую биржу

                result.put(symbol, tickers);
            }

            return result;
        } catch (Exception e) {
            log.error("Ошибка при агрегации данных: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }
}
