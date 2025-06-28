package bybit.bybit_exel.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExportRequest {
    private String startTime;
    private String endTime;
    private String symbol;
    private String interval;
    private int columnsCount;
    private List<ColumnInfo> columns;

    @Data
    public static class ColumnInfo {
        private String type;
        private String exchange;
    }
}
