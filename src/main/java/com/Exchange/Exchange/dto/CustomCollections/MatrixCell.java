package com.Exchange.Exchange.dto.CustomCollections;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MatrixCell {
    private String buyExchange;
    private String sellExchange;
    private Double ProfitPercent;
    private Double buyPrice;
    private Double sellPrice;
}
