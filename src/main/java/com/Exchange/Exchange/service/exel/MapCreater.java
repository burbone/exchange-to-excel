package com.Exchange.Exchange.service.exel;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapCreater {
    public static Map<Long, List<String>> mapCreate(String interval, String timeStart, String timeEnd) {
        Map<Long, List<String>> timeAndPrices = new HashMap<>();

        List<Long> times = TimeRemodeling.timeSplitter(interval, timeStart, timeEnd);

        for(long time : times) timeAndPrices.put(time, new ArrayList<>());
        return timeAndPrices;
    }

}
