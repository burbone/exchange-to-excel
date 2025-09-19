package Exchange.service;

import Exchange.api.NowPricesApi;
import Exchange.model.CommonPair;
import Exchange.util.PairComparator;

import java.util.List;
import java.util.Map;

public class PricesComparisonService {
    
    private final ExchangeApiFactory apiFactory;
    private final PairComparator pairComparator;
    
    public PricesComparisonService() {
        this.apiFactory = new ExchangeApiFactory();
        this.pairComparator = new PairComparator();
    }
    
    public List<CommonPair> comparePrices(String exchange1Name, String exchange2Name) throws Exception {
        NowPricesApi exchange1Api = apiFactory.createApi(exchange1Name);
        NowPricesApi exchange2Api = apiFactory.createApi(exchange2Name);
        
        Map<String, String> exchange1Data = exchange1Api.getPrices();
        Map<String, String> exchange2Data = exchange2Api.getPrices();
        
        return pairComparator.findCommonPairs(exchange1Data, exchange2Data);
    }
} 