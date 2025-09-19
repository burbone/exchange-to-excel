package Exchange.util;

import Exchange.model.CommonPair;
import java.util.*;

public class PairComparator {
    public List<CommonPair> findCommonPairs(Map<String, String> exchange1Data, Map<String, String> exchange2Data) {
        Map<String, String> exchange2Normalized = new HashMap<>();
        
        for (String exchange2Pair : exchange2Data.keySet()) {
            String normalized = exchange2Pair.replace("-", "");
            exchange2Normalized.put(normalized, exchange2Pair);
        }
        
        List<CommonPair> commonPairs = new ArrayList<>();
        
        for (String exchange1Pair : exchange1Data.keySet()) {
            if (exchange2Normalized.containsKey(exchange1Pair)) {
                String exchange2Pair = exchange2Normalized.get(exchange1Pair);
                String exchange1Price = exchange1Data.get(exchange1Pair);
                String exchange2Price = exchange2Data.get(exchange2Pair);
                
                commonPairs.add(new CommonPair(exchange1Pair, exchange2Pair, exchange1Price, exchange2Price));
            }
        }
        
        return commonPairs;
    }
} 