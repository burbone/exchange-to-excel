package Exchange.service;

import Exchange.dto.ExportRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {
    private Workbook workbook;
    private Sheet sheet;

    public ExcelService() {
        this.workbook = new HSSFWorkbook();
        this.sheet = workbook.createSheet();
    }

    public void fillTable(List<Long> times, List<ExportRequest.ColumnInfo> columns, Map<Long, Map<String, Double>> data) {
        createHeader(columns);
        fillData(times, columns, data);
    }

    private void createHeader(List<ExportRequest.ColumnInfo> columns) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("timestamp");
        
        for (int i = 0; i < columns.size(); i++) {
            String label = columns.get(i).getExchange() + ":" + columns.get(i).getType();
            header.createCell(i + 1).setCellValue(label);
        }
    }

    private void fillData(List<Long> times, List<ExportRequest.ColumnInfo> columns, Map<Long, Map<String, Double>> data) {
        for (int i = 0; i < times.size(); i++) {
            Long timestamp = times.get(i);
            Row row = sheet.createRow(i + 1);
            
            row.createCell(0).setCellValue(timestamp);
            
            Map<String, Double> rowData = data.getOrDefault(timestamp, Map.of());
            
            for (int j = 0; j < columns.size(); j++) {
                String key = columns.get(j).getExchange() + ":" + columns.get(j).getType();
                Double value = rowData.get(key);
                
                if (value != null) {
                    row.createCell(j + 1).setCellValue(value);
                }
            }
        }
    }

    public byte[] toByteArray() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            return bos.toByteArray();
        }
    }
} 