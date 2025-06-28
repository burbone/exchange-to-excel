package bybit.bybit_exel.model;

import lombok.Data;

@Data
public class Candle {
    private long timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private String exchange;

    public Candle () {
    }

    public Candle(long timestamp, double open, double high,
                  double low, double close) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public Candle(long timestamp, double open, double high,
                  double low, double close, String exchange) {
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.exchange = exchange;
    }
}
