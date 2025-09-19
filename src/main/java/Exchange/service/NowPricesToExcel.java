package Exchange.service;

import Exchange.model.CommonPair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

public class NowPricesToExcel {
    
    public void saveToExcel(String filename, List<CommonPair> pairs) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Crypto Prices");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Symbol");
        headerRow.createCell(1).setCellValue("Exchange 1 Price");
        headerRow.createCell(2).setCellValue("Exchange 2 Price");

        int rowNum = 1;
        for (CommonPair pair : pairs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pair.getExchange1Pair());
            row.createCell(1).setCellValue(pair.getExchange1Price());
            row.createCell(2).setCellValue(pair.getExchange2Price());
        }

        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
    
    public byte[] generateExcelBytes(List<CommonPair> pairs) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Crypto Prices");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Symbol");
        headerRow.createCell(1).setCellValue("Exchange 1 Price");
        headerRow.createCell(2).setCellValue("Exchange 2 Price");

        int rowNum = 1;
        for (CommonPair pair : pairs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pair.getExchange1Pair());
            row.createCell(1).setCellValue(pair.getExchange1Price());
            row.createCell(2).setCellValue(pair.getExchange2Price());
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        }
    }
}
