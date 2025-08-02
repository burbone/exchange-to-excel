package bybit.bybit_exel.util;

import org.springframework.stereotype.Component;

@Component
public class IntervalUtils {
    public static long getIntervalDurationMs(String interval) {
        return switch (interval) {
            case "1m" -> 60 * 1000L;
            case "3m" -> 3 * 60 * 1000L;
            case "5m" -> 5 * 60 * 1000L;
            case "15m" -> 15 * 60 * 1000L;
            case "30m" -> 30 * 60 * 1000L;
            case "1h" -> 60 * 60 * 1000L;
            case "2h" -> 2 * 60 * 60 * 1000L;
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
            case "2h" -> "120";
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
            case "2h" -> "2hour";
            case "4h" -> "4hour";
            case "6h" -> "6hour";
            case "12h" -> "12hour";
            default -> interval;
        };
    }
    
    public static boolean isValidInterval(String interval) {
        return switch (interval) {
            case "1m", "3m", "5m", "15m", "30m", "1h", "2h", "4h", "6h", "12h" -> true;
            default -> false;
        };
    }
    
    public static String[] getSupportedIntervals() {
        return new String[]{"1m", "3m", "5m", "15m", "30m", "1h", "2h", "4h", "6h", "12h"};
    }
} 