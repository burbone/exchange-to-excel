package com.Exchange.Exchange.service.exel;

import org.springframework.stereotype.Component;

@Component
public class IntervalRemodelling {

    public static long getInterval(String interval) {
        return switch (interval) {
            case "1m" -> 60 * 1000L;
            case "3m" -> 3 * 60 * 1000L;
            case "5m" -> 5 * 60 * 1000L;
            case "15m" -> 15 * 60 * 1000L;
            case "30m" -> 30 * 60 * 1000L;
            case "1h" -> 60 * 60 * 1000L;
            case "4h" -> 4 * 60 * 60 * 1000L;
            case "6h" -> 6 * 60 * 60 * 1000L;
            case "12h" -> 12 * 60 * 60 * 1000L;
            default -> 60 * 1000L;
        };
    }

    public static String toBybitFormat(String interval) {
        return switch (interval) {
            case "1m" -> "1";
            case "3m" -> "3";
            case "5m" -> "5";
            case "15m" -> "15";
            case "30m" -> "30";
            case "1h" -> "60";
            case "4h" -> "240";
            case "6h" -> "360";
            case "12h" -> "720";
            default -> interval;
        };
    }

    public static String toKucoinFormat(String interval) {
        return switch (interval) {
            case "1m" -> "1min";
            case "3m" -> "3min";
            case "5m" -> "5min";
            case "15m" -> "15min";
            case "30m" -> "30min";
            case "1h" -> "1hour";
            case "4h" -> "4hour";
            case "6h" -> "6hour";
            case "12h" -> "12hour";
            default -> interval;
        };
    }
    public static String toBitgetFormat(String interval) {
        return switch (interval) {
            case "1m" -> "1min";
            case "3m" -> "3min";
            case "5m" -> "5min";
            case "15m" -> "15min";
            case "30m" -> "30min";
            case "1h" -> "1h";
            case "4h" -> "4h";
            case "6h" -> "6h";
            case "12h" -> "12h";
            default -> interval;
        };
    }
    public static String toOKXFormat(String interval) {
        return switch (interval) {
            case "1m" -> "1m";
            case "3m" -> "3m";
            case "5m" -> "5m";
            case "15m" -> "15m";
            case "30m" -> "30m";
            case "1h" -> "1H";
            case "4h" -> "4H";
            case "6h" -> "6H";
            case "12h" -> "12H";
            default -> interval;
        };
    }
}