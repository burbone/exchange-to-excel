package com.Exchange.Exchange.config;

import com.Exchange.Exchange.handler.ClientWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ActivityChecker {

    @Scheduled(fixedRate = 5000)
    public void checkActivity() {
        if (ClientWebSocketHandler.hasActiveConnections()) {
            long inactiveTime = System.currentTimeMillis() - ClientWebSocketHandler.getLastActivityTime();
            if (inactiveTime > 120000) {
                System.out.println("No activity for 2 minutes, shutting down...");
                System.exit(0);
            }
        }
    }
}