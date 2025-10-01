package com.Exchange.Exchange.service.tickers;

import com.Exchange.Exchange.dto.CustomCollections.ArbitrageResult;
import com.Exchange.Exchange.dto.CustomCollections.Ticker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopFive {

    private final PriceRepo priceRepo;

    public List<ArbitrageResult> getTopFive() {
        Map<String, List<Ticker>> allPairs = priceRepo.getCachedData();
        List<ArbitrageResult> results = new ArrayList<>();

        for (String symbol : allPairs.keySet()) {
            List<Ticker> tickers = allPairs.get(symbol);

            List<Ticker> validTickers = tickers.stream()
                    .filter(ticker -> isValidTicker(ticker))
                    .collect(Collectors.toList());

            if (validTickers.size() < 2) {
                continue;
            }

            Ticker minAskTicker = validTickers.stream()
                    .min(Comparator.comparing(Ticker::getAskPrice))
                    .orElse(null);

            Ticker maxBidTicker = validTickers.stream()
                    .max(Comparator.comparing(Ticker::getBidPrice))
                    .orElse(null);

            if (minAskTicker != null && maxBidTicker != null
                    && !minAskTicker.getExchange().equals(maxBidTicker.getExchange())) {

                Double buyPrice = minAskTicker.getAskPrice();
                Double sellPrice = maxBidTicker.getBidPrice();

                double profitPercent = ((sellPrice - buyPrice) / buyPrice) * 100;

                if (profitPercent > 0.01 && profitPercent <= 100.0) {
                    double priceRatio = sellPrice / buyPrice;

                    if (priceRatio <= 2.0 && priceRatio >= 0.5) {
                        results.add(new ArbitrageResult(
                                symbol,
                                minAskTicker.getExchange(),
                                maxBidTicker.getExchange(),
                                buyPrice,
                                sellPrice,
                                profitPercent
                        ));
                    }
                }
            }
        }

        return results.stream()
                .sorted((a, b) -> Double.compare(b.getProfitPercent(), a.getProfitPercent()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private boolean isValidTicker(Ticker ticker) {
        if (ticker == null) {
            return false;
        }

        Double bidPrice = ticker.getBidPrice();
        Double askPrice = ticker.getAskPrice();

        if (!isValidPrice(bidPrice)) {
            return false;
        }

        if (!isValidPrice(askPrice)) {
            return false;
        }

        if (askPrice < bidPrice) {
            log.debug("Некорректный спред для {}: bid={}, ask={}",
                    ticker.getExchange(), bidPrice, askPrice);
            return false;
        }

        double spread = ((askPrice - bidPrice) / bidPrice) * 100;
        if (spread > 10.0) {
            log.debug("Слишком большой спред для {}: {}%",
                    ticker.getExchange(), spread);
            return false;
        }

        return true;
    }

    private boolean isValidPrice(Double price) {
        if (price == null || price <= 0) {
            return false;
        }

        if (price < 0.00001) {
            return false;
        }

        if (price > 1000000) {
            return false;
        }

        return Double.isFinite(price);
    }
}