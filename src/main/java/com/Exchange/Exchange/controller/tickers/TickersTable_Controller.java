package com.Exchange.Exchange.controller.tickers;

import com.Exchange.Exchange.service.tickers.CreateTable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TickersTable_Controller {

    /*
    Добавить биржу:
    1 - PriceRepo
     */

    private final CreateTable createTable;

    @GetMapping("/matrix/{symbol}")
    public ResponseEntity<Map<String, Object>> getMatrix(@PathVariable String symbol) {
        try {
            Map<String, Object> matrixData = createTable.getFrontendData(symbol);
            return ResponseEntity.ok(matrixData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
