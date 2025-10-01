package com.Exchange.Exchange.dto.Request;

import lombok.Data;

import java.util.List;

@Data
public class Export_Request {
    private String startTime;
    private String endTime;
    private String symbol;
    private String interval;
    private List<ColumnInfo> columns;

    @Data
    public static class ColumnInfo {
        private String type;
        private String exchange;
    }
}
