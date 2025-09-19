package Exchange.service;

import Exchange.util.IntervalUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeRangeSplitter {
    
    private static final int CANDLES_PER_REQUEST = 1000;
    
    public static class TimePart {
        private final long startTime;
        private final long endTime;
        private final int partNumber;
        
        public TimePart(long startTime, long endTime, int partNumber) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.partNumber = partNumber;
        }
        
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public int getPartNumber() { return partNumber; }
        
        @Override
        public String toString() {
            return String.format("Part %d: %s - %s", 
                partNumber, 
                formatTime(startTime), 
                formatTime(endTime));
        }
        
        private String formatTime(long timestamp) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                    .toString().replace('T', ' ');
        }
    }
    
    public List<TimePart> splitTimeRange(long startTime, long endTime, String userInterval) {
        List<TimePart> parts = new ArrayList<>();
        
        long intervalDuration = IntervalUtils.getIntervalDurationMs(userInterval);
        long totalCandles = (endTime - startTime) / intervalDuration;
        int partsCount = (int) Math.ceil((double) totalCandles / CANDLES_PER_REQUEST);
        
        for (int i = 0; i < partsCount; i++) {
            long partStartTime = startTime + (i * CANDLES_PER_REQUEST * intervalDuration);
            long partEndTime = Math.min(
                partStartTime + (CANDLES_PER_REQUEST * intervalDuration), 
                endTime
            );
            
            TimePart part = new TimePart(partStartTime, partEndTime, i + 1);
            parts.add(part);
        }
        
        return parts;
    }
} 