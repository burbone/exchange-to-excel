package bybit.bybit_exel.Controller;

import bybit.bybit_exel.dto.ExportRequest;
import bybit.bybit_exel.service.ColumnsInfoService;
import bybit.bybit_exel.service.DataCollectorService;
import bybit.bybit_exel.service.DataProcessingService;
import bybit.bybit_exel.service.ExchangeProcessor;
import bybit.bybit_exel.service.ExcelService;
import bybit.bybit_exel.service.ValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ExportController {

    private final DataCollectorService dataCollectorService;
    private final ExchangeProcessor exchangeProcessor;
    private final DataProcessingService dataProcessingService;
    private final ExcelService excelService;
    private final ColumnsInfoService columnsInfoService;
    private final ValidationService validationService;

    public ExportController(DataCollectorService dataCollectorService,
                          ExchangeProcessor exchangeProcessor,
                          DataProcessingService dataProcessingService,
                          ExcelService excelService,
                          ColumnsInfoService columnsInfoService,
                          ValidationService validationService) {
        this.dataCollectorService = dataCollectorService;
        this.exchangeProcessor = exchangeProcessor;
        this.dataProcessingService = dataProcessingService;
        this.excelService = excelService;
        this.columnsInfoService = columnsInfoService;
        this.validationService = validationService;
    }

    @PostMapping("/export-table")
    public ResponseEntity<?> exportTable(@RequestBody ExportRequest request) {
        try {
            validationService.validateExportRequest(request);
            
            List<String> exchanges = extractExchanges(request.getColumns());

            Map<String, bybit.bybit_exel.api.ExchangeDataPrepare> preparedData = 
                exchangeProcessor.processExchanges(exchanges, request.getSymbol(), request.getInterval(), 
                                                 request.getStartTime(), request.getEndTime());

            columnsInfoService.columsSetter(request.getColumns());
            List<bybit.bybit_exel.model.ColumnsInfo> columnsInfo = columnsInfoService.getColumns();

            List<bybit.bybit_exel.model.Candle> candles = dataCollectorService.collectData(preparedData, columnsInfo, request.getInterval());

            List<Long> times = dataProcessingService.extractTimestamps(candles, columnsInfo);
            Map<Long, Map<String, Double>> dataMap = dataProcessingService.createDataMap(candles, columnsInfo);

            excelService.fillTable(times, request.getColumns(), dataMap);
            byte[] excelBytes = excelService.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(excelBytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .contentLength(excelBytes.length)
                    .body(resource);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при обработке запроса: " + e.getMessage());
        }
    }

    private List<String> extractExchanges(List<ExportRequest.ColumnInfo> columns) {
        return columns.stream()
                .map(col -> col.getExchange().toLowerCase())
                .distinct()
                .collect(Collectors.toList());
    }
}
