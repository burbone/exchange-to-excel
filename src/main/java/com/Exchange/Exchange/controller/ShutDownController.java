package com.Exchange.Exchange.controller;

import com.Exchange.Exchange.ExchangeApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutDownController {

    private final ExchangeApplication exchangeApplication;

    public ShutDownController(ExchangeApplication exchangeApplication) {
        this.exchangeApplication = exchangeApplication;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health(@RequestParam(required = false) String action) {
        System.out.println("Health endpoint called, action: " + action);

        if ("close".equals(action)) {
            System.out.println("Получен сигнал закрытия от браузера");
            exchangeApplication.initiateShutdown();
            return ResponseEntity.ok("closing");
        }
        return ResponseEntity.ok("ok");
    }
}