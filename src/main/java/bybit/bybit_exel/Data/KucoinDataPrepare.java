package bybit.bybit_exel.Data;

import bybit.bybit_exel.api.ExchangeDataPrepare;
import bybit.bybit_exel.util.IntervalUtils;
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
        return IntervalUtils.toKucoinFormat(interval);
    }

    @Override
    public long prepareStartTime(String startTime) {
        LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public long prepareEndTime(String endTime) {
        LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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
