package com.Exchange.Exchange.service.exel;

import com.Exchange.Exchange.dto.Request.Export_Request;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExelCreate {

    public static Workbook createExcel(List<Export_Request.ColumnInfo> columns,
                                       Map<Long, List<String>> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row headerRow = sheet.createRow(0);
        Cell timeCell = headerRow.createCell(0);
        timeCell.setCellValue("Timestamp");

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i + 1);
            Export_Request.ColumnInfo column = columns.get(i);
            cell.setCellValue(column.getExchange() + " " + column.getType());
        }

        List<Long> sortedTimes = new ArrayList<>(data.keySet());
        sortedTimes.sort(Long::compareTo);

        for (int i = 0; i < sortedTimes.size(); i++) {
            Long timestamp = sortedTimes.get(i);
            Row row = sheet.createRow(i + 1);

            Cell timeCell2 = row.createCell(0);
            timeCell2.setCellValue(timestamp);

            List<String> rowData = data.get(timestamp);
            if (rowData != null) {
                for (int j = 0; j < rowData.size(); j++) {
                    Cell dataCell = row.createCell(j + 1);
                    String value = rowData.get(j);
                    dataCell.setCellValue(value);
                }
            }
        }

        return workbook;
    }
}
