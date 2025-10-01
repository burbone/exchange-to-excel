package com.Exchange.Exchange.dto.CustomCollections;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ArbitrageMatrix {
    private List<String> exchange;
    private Map<String, Map<String, Double>> matrix;
    private List<MatrixCell> sortedCells;
    private List<String> sellExchange;
}
