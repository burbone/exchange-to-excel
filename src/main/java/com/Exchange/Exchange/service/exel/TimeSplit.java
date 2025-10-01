package com.Exchange.Exchange.service.exel;

import java.util.ArrayList;
import java.util.List;

public class TimeSplit {
    private List<TimeRange> splitTime(String interval, String timeStart, String timeEnd) {
        List<TimeRange> splitedTime = new ArrayList<>();

        long timeS = TimeRemodeling.timeRemodelling(timeStart);
        long timeE = TimeRemodeling.timeRemodelling(timeEnd);
        long inter = IntervalRemodelling.getInterval(interval) * 1000;

        long totalTime = timeE - timeS;
        long partSize = totalTime / inter;

        for (int i = 0; i < inter; i++) {
            long partStart = timeS + (i * partSize);
            long partEnd = (i == inter - 1) ? timeE : timeS + ((i + 1) * partSize);
            splitedTime.add(new TimeRange(partStart, partEnd));
        }

        return splitedTime;
    }

    private static class TimeRange {
        final long start;
        final long end;

        TimeRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
