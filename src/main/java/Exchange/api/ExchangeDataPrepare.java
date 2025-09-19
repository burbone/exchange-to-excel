package Exchange.api;

public interface ExchangeDataPrepare {
    String prepareSymbol(String symbol);
    String prepareInterval(String interval);
    long prepareStartTime(String startTime);
    long prepareEndTime(String endTime);
    String getExchangeName();
    void setAll(String symbol, String interval, String startTime, String endTime);

    String getSymbol();
    String getInterval();
    long getStartTime();
    long getEndTime();
} 