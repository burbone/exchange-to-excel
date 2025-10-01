package com.Exchange.Exchange.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionPages = new ConcurrentHashMap<>();
    @Getter
    private static long lastActivityTime = System.currentTimeMillis();
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static boolean shutdownScheduled = false;
    private static final Map<String, Boolean> sessionNavigating = new ConcurrentHashMap<>();
    private static final long NAVIGATION_GRACE_PERIOD = 10000;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        sessionNavigating.put(sessionId, false);
        lastActivityTime = System.currentTimeMillis();

        if (shutdownScheduled) {
            System.out.println("Новое подключение во время ожидания shutdown, отменяем завершение");
            shutdownScheduled = false;
        }

        System.out.println("WebSocket connected: " + sessionId + ", Total connections: " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        sessionPages.remove(sessionId);

        Boolean wasNavigating = sessionNavigating.remove(sessionId);

        System.out.println("WebSocket disconnected: " + sessionId +
                ", Status: " + status +
                ", Was navigating: " + wasNavigating +
                ", Remaining connections: " + sessions.size());

        if (Boolean.TRUE.equals(wasNavigating)) {
            scheduleNavigationShutdownCheck();
        } else if (sessions.isEmpty() && !shutdownScheduled) {
            shutdownScheduled = true;
            scheduleShutdownCheck();
        }
    }

    private void scheduleNavigationShutdownCheck() {
        if (!shutdownScheduled) {
            shutdownScheduled = true;
            System.out.println("Запланировано ожидание новых подключений на " + (NAVIGATION_GRACE_PERIOD/1000) + " секунд");
            scheduler.schedule(() -> {
                if (sessions.isEmpty()) {
                    System.out.println("Нет новых подключений после навигации, завершаем приложение...");
                    initiateShutdown();
                } else {
                    shutdownScheduled = false;
                    System.out.println("Появилось новое соединение после навигации, отменяем shutdown");
                }
            }, NAVIGATION_GRACE_PERIOD, TimeUnit.MILLISECONDS);
        }
    }

    private void scheduleShutdownCheck() {
        scheduler.schedule(() -> {
            if (sessions.isEmpty()) {
                System.out.println("Все WebSocket соединения закрыты, завершаем приложение...");
                initiateShutdown();
            } else {
                shutdownScheduled = false;
                System.out.println("Появились новые соединения, отменяем shutdown");
            }
        }, 2, TimeUnit.SECONDS);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        lastActivityTime = System.currentTimeMillis();

        try {
            String payload = message.getPayload();
            if (payload.startsWith("{")) {
                JsonNode json = new ObjectMapper().readTree(payload);
                if (json.has("type")) {
                    String type = json.get("type").asText();
                    if ("page_info".equals(type) && json.has("page")) {
                        String page = json.get("page").asText();
                        sessionPages.put(session.getId(), page);
                        System.out.println("Session " + session.getId() + " on page: " + page);
                    } else if ("navigation".equals(type)) {
                        sessionNavigating.put(session.getId(), true);
                        System.out.println("Session " + session.getId() + " начал навигацию");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing message: " + e.getMessage());
        }
    }

    public static boolean hasActiveConnections() {
        return !sessions.isEmpty();
    }

    public static int getActiveConnectionsCount() {
        return sessions.size();
    }

    private void initiateShutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}