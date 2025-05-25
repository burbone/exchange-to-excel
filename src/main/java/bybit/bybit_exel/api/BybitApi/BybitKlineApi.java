package bybit.bybit_exel.api.BybitApi;

import java.util.List;

public class BybitKlineApi {
    public List<?>[] getInfo (String symbol, String interval, long startTime, long endTime, String[] params) {
        String start = String.valueOf(startTime);
        String end = String.valueOf(endTime);
        
        String url = "api-testnet.bybit.com/v5/market/kline?category=inverse" +
                "&symbol=" + symbol +
                "&interval=" + interval +
                "&start=" + start +
                "&end=" + end;
}
