package com.Exchange.Exchange.controller.tickers;

import com.Exchange.Exchange.dto.CustomCollections.ArbitrageResult;
import com.Exchange.Exchange.service.tickers.TopFive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TopTickers_Controller {

    /*
    обавить новую биржу:
    1 - PriceRepo - добавить в 2 местах
     */
    private final TopFive topFive;

    @GetMapping("/top5")
    public ResponseEntity<List<ArbitrageResult>> getTopFive() {
        try {
            List<ArbitrageResult> topResults = topFive.getTopFive();
            return ResponseEntity.ok(topResults);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
