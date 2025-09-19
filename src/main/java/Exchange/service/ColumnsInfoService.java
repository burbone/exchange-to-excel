package Exchange.service;

import Exchange.dto.ExportRequest;
import Exchange.model.ColumnsInfo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ColumnsInfoService {

    private List<ColumnsInfo> columns = Collections.emptyList();

    public void columsSetter(List<ExportRequest.ColumnInfo> columns) {
        this.columns = columns.stream()
                .map(col -> new ColumnsInfo(col.getExchange(), col.getType()))
                .toList();
    }

    public List<ColumnsInfo> getColumns() {
        return columns;
    }
}
