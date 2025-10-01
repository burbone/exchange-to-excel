package com.Exchange.Exchange.dto.CustomCollections;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArbitrageResult {
    private String symbol;
    private String buyExchange;
    private String sellExchange;
    private Double buyPrice;
    private Double sellPrice;
    private Double profitPercent;
}
