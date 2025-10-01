package com.Exchange.Exchange.controller;

import com.Exchange.Exchange.handler.ClientWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StatusController {

    @GetMapping("/api/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeConnections", ClientWebSocketHandler.getActiveConnectionsCount());
        status.put("lastActivity", ClientWebSocketHandler.getLastActivityTime());
        status.put("shutdownScheduled", false);
        return ResponseEntity.ok(status);
    }
}
