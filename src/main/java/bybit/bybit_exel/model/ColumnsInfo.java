package bybit.bybit_exel.model;

import lombok.Data;

@Data
public class ColumnsInfo {

    private String exchange;
    private String dataType;

    public ColumnsInfo() {
    }

    public ColumnsInfo(String exchange, String dataType) {
        this.exchange = exchange;
        this.dataType = dataType;
    }
}
