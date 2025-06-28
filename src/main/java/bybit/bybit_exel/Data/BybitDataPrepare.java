package bybit.bybit_exel.Data;

import bybit.bybit_exel.api.ExchangeDataPrepare;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
public class BybitDataPrepare implements ExchangeDataPrepare {
    private String symbol = "";
    private String interval = "";
    private long startTime = 0;
    private long endTime = 0;

    @Override
    public String prepareSymbol(String symbol) {
        return symbol.replaceAll(" ", "");
    }

    @Override
    public String prepareInterval(String interval) {
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

    @Override
    public long prepareStartTime(String startTime) {
        return parseDateTimeFlexible(startTime);
    }

    @Override
    public long prepareEndTime(String endTime) {
        return parseDateTimeFlexible(endTime);
    }

    @Override
    public String getExchangeName() {
        return "bybit";
    }

    private long parseDateTimeFlexible(String value) {
        if (value == null) return 0;
        try {
            return LocalDateTime.parse(value.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (Exception ignored) {}
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (Exception ignored) {}
        throw new IllegalArgumentException("Неверный формат времени: " + value);
    }

    public void setAll(String symbol, String interval, String startTime, String endTime) {
        this.symbol = prepareSymbol(symbol);
        this.interval = prepareInterval(interval);
        this.startTime = prepareStartTime(startTime);
        this.endTime = prepareEndTime(endTime);
    }
}
