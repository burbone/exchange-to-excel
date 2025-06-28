package bybit.bybit_exel.Data;

import bybit.bybit_exel.api.ExchangeDataPrepare;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
public class KucoinDataPrepare implements ExchangeDataPrepare {
    private String symbol = "";
    private String interval = "";
    private long startTime = 0;
    private long endTime = 0;

    @Override
    public String prepareSymbol(String symbol) {
        return symbol.replaceAll(" ", "-");
    }

    @Override
    public String prepareInterval(String interval) {
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

    @Override
    public long prepareStartTime(String startTime) {
        LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
    }

    @Override
    public long prepareEndTime(String endTime) {
        LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
    }

    @Override
    public String getExchangeName() {
        return "kucoin";
    }

    public void setAll(String symbol, String interval, String startTime, String endTime) {
        this.symbol = prepareSymbol(symbol);
        this.interval = prepareInterval(interval);
        this.startTime = prepareStartTime(startTime);
        this.endTime = prepareEndTime(endTime);
    }
}
