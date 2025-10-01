package com.Exchange.Exchange.service.tickers;

import com.Exchange.Exchange.dto.CustomCollections.ArbitrageMatrix;
import com.Exchange.Exchange.dto.CustomCollections.MatrixCell;
import com.Exchange.Exchange.dto.CustomCollections.Ticker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateTable {
    private final GetPricesBySymbol getPricesBySymbol;

    public ArbitrageMatrix buildMatrix(String symbol) {
        List<Ticker> tickers = getPricesBySymbol.getPricesBySimbol(symbol);
        ArbitrageMatrix arbitrageMatrix = new ArbitrageMatrix();

        if (tickers.isEmpty()) {
            return arbitrageMatrix;
        }

        String bestBuyExchange = null;
        String bestSellExchange = null;
        double maxProfit = Double.NEGATIVE_INFINITY;

        for (Ticker buyTicker : tickers) {
            for (Ticker sellTicker : tickers) {
                if (!buyTicker.getExchange().equals(sellTicker.getExchange())) {
                    double profit = ((sellTicker.getBidPrice() - buyTicker.getAskPrice())
                            / buyTicker.getAskPrice()) * 100;
                    if (profit > maxProfit) {
                        maxProfit = profit;
                        bestBuyExchange = buyTicker.getExchange();
                        bestSellExchange = sellTicker.getExchange();
                    }
                }
            }
        }

        List<String> buyExchanges = new ArrayList<>();
        List<String> sellExchanges = new ArrayList<>();

        if (bestBuyExchange != null) {
            buyExchanges.add(bestBuyExchange);
        }
        if (bestSellExchange != null) {
            sellExchanges.add(bestSellExchange);
        }

        for (Ticker ticker : tickers) {
            String exchange = ticker.getExchange();
            if (!exchange.equals(bestBuyExchange)) {
                buyExchanges.add(exchange);
            }
            if (!exchange.equals(bestSellExchange)) {
                sellExchanges.add(exchange);
            }
        }

        Map<String, Map<String, Double>> matrix = new LinkedHashMap<>();
        List<MatrixCell> allCells = new ArrayList<>();

        Map<String, Double> askPriceMap = tickers.stream()
                .collect(Collectors.toMap(Ticker::getExchange, Ticker::getAskPrice));
        Map<String, Double> bidPriceMap = tickers.stream()
                .collect(Collectors.toMap(Ticker::getExchange, Ticker::getBidPrice));

        for (String buyExchange : buyExchanges) {
            Map<String, Double> row = new LinkedHashMap<>();
            Double askPrice = askPriceMap.get(buyExchange);

            for (String sellExchange : sellExchanges) {
                if (buyExchange.equals(sellExchange)) {
                    row.put(sellExchange, 0.0);
                } else {
                    Double bidPrice = bidPriceMap.get(sellExchange);

                    Double profitPercent = ((bidPrice - askPrice) / askPrice) * 100;
                    row.put(sellExchange, profitPercent);

                    MatrixCell cell = new MatrixCell();
                    cell.setBuyExchange(buyExchange);
                    cell.setSellExchange(sellExchange);
                    cell.setProfitPercent(profitPercent);
                    cell.setBuyPrice(askPrice);
                    cell.setSellPrice(bidPrice);
                    allCells.add(cell);
                }
            }
            matrix.put(buyExchange, row);
        }

        List<MatrixCell> sortedCells = allCells.stream()
                .sorted((a, b) -> Double.compare(b.getProfitPercent(), a.getProfitPercent()))
                .collect(Collectors.toList());

        arbitrageMatrix.setExchange(buyExchanges);
        arbitrageMatrix.setSellExchange(sellExchanges);
        arbitrageMatrix.setMatrix(matrix);
        arbitrageMatrix.setSortedCells(sortedCells);

        return arbitrageMatrix;
    }

    public Map<String, Object> getFrontendData(String symbol) {
        ArbitrageMatrix matrix = buildMatrix(symbol);

        Map<String, Object> result = new HashMap<>();
        result.put("symbol", symbol);
        result.put("buyExchanges", matrix.getExchange());
        result.put("sellExchanges", matrix.getSellExchange());
        result.put("matrix", matrix.getMatrix());
        result.put("sortedCells", matrix.getSortedCells());

        return result;
    }

    public double[][] getMatrixArray(String symbol) {
        ArbitrageMatrix matrix = buildMatrix(symbol);
        List<String> buyExchanges = matrix.getExchange();
        List<String> sellExchanges = matrix.getSellExchange();
        Map<String, Map<String, Double>> matrixData = matrix.getMatrix();

        if (buyExchanges.isEmpty()) {
            return new double[0][0];
        }

        double[][] result = new double[buyExchanges.size()][sellExchanges.size()];

        for (int i = 0; i < buyExchanges.size(); i++) {
            String buyExchange = buyExchanges.get(i);
            Map<String, Double> row = matrixData.get(buyExchange);

            for (int j = 0; j < sellExchanges.size(); j++) {
                String sellExchange = sellExchanges.get(j);
                result[i][j] = row.get(sellExchange);
            }
        }

        return result;
    }
}
