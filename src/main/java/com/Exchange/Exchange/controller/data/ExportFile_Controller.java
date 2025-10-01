package com.Exchange.Exchange.controller.data;

import com.Exchange.Exchange.dto.Request.Export_Request;
import com.Exchange.Exchange.service.exel.ExelCreate;
import com.Exchange.Exchange.service.exel.MapFill;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExportFile_Controller {
    /*
    добавить новую биржи
    1 - создать файл в \api\Биржа\KlineБиржа_api
    2 - создать коннектор в \exel\connector\KlineБиржаConnector
    3 - добавить данные в service\exel\MapFill.java
    4 - добавить биржу в data.html и main.js
    5 - добавить обработку в service\exel\IntervalRemodelling.java
    6 - добить отчистку service\exel\ClearAllData.java
     */
    @Autowired
    private MapFill mapFill;

    @PostMapping("/exportfile")
    public ResponseEntity<?> exportFile(@RequestBody Export_Request request) {
        try {
            String symbol = request.getSymbol();
            String interval = request.getInterval();
            String startTime = request.getStartTime();
            String endTime = request.getEndTime();
            List<Export_Request.ColumnInfo> columns = request.getColumns();

            if (symbol == null || symbol.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Символ не может быть пустым");
            }

            if (startTime == null || endTime == null) {
                return ResponseEntity.badRequest()
                        .body("Время начала и окончания обязательны");
            }

            if (columns == null || columns.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Необходимо указать хотя бы одну колонку");
            }

            mapFill.mapFill(symbol, interval, startTime, endTime, columns);
            Map<Long, List<String>> data = mapFill.getData();

            if (data.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Не удалось получить данные для указанного периода и символа");
            }

            Workbook workbook = ExelCreate.createExcel(columns, data);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            byte[] excelBytes = outputStream.toByteArray();
            outputStream.close();

            String fileName = generateFileName(symbol, interval, startTime, endTime);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Ошибка создания файла: " + e.getMessage());
        }
    }

    private String generateFileName(String symbol, String interval, String startTime, String endTime) {
        String cleanSymbol = symbol.replaceAll("[^a-zA-Z0-9]", "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        return String.format("exchange_data_%s_%s_%s.xlsx", cleanSymbol, interval, currentDate);
    }
}