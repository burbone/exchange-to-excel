package Exchange.Controller;

import Exchange.model.CommonPair;
import Exchange.service.NowPricesToExcel;
import Exchange.service.PricesComparisonService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NowPricesController {

    private final PricesComparisonService comparisonService;
    private final NowPricesToExcel excelGenerator;

    public NowPricesController() {
        this.comparisonService = new PricesComparisonService();
        this.excelGenerator = new NowPricesToExcel();
    }

    @PostMapping("/download-prices")
    public ResponseEntity<byte[]> downloadPrices(@RequestBody Map<String, String> request) {
        try {
            String exchangeOne = request.get("exchangeOne");
            String exchangeSecond = request.get("exchangeSecond");
            
            List<CommonPair> commonPairs = comparisonService.comparePrices(exchangeOne, exchangeSecond);
            
            byte[] excelBytes = excelGenerator.generateExcelBytes(commonPairs);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "prices_comparison.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
