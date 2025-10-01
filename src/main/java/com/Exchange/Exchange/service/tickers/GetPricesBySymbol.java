package com.Exchange.Exchange.service.tickers;

import com.Exchange.Exchange.dto.CustomCollections.ArbitrageResult;
import com.Exchange.Exchange.dto.CustomCollections.Ticker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetPricesBySymbol {

    private final PriceRepo priceRepo;

    public List<Ticker> getPricesBySimbol (String symbol) {
        Map<String, List<Ticker>> cachedData = priceRepo.getCachedData();
        List<Ticker> result = cachedData.get(symbol);

        if (result == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(result);
    }
}
