package com.Exchange.Exchange.service.exel;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeRemodeling {
    public static long timeRemodelling(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static List<Long> timeSplitter(String interval, String timeStart, String timeEnd) {
        List<Long> times = new ArrayList<>();

        long timeS = TimeRemodeling.timeRemodelling(timeStart);
        long timeE = TimeRemodeling.timeRemodelling(timeEnd);
        long inter = IntervalRemodelling.getInterval(interval);

        for(; timeS < timeE; timeS += inter) times.add(timeS);
        return times;
    }
}
