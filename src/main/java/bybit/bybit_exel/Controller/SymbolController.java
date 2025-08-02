package bybit.bybit_exel.Controller;

import bybit.bybit_exel.api.BybitApi.BybitSymbolApi;
import bybit.bybit_exel.api.KucoinApi.KucoinSymbolApi;
import bybit.bybit_exel.dto.SymbolCheckRequest;
import bybit.bybit_exel.dto.SymbolCheckResponse;
import bybit.bybit_exel.service.SymbolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SymbolController {

    private final BybitSymbolApi bybitSymbolApi = new BybitSymbolApi();
    private final KucoinSymbolApi kucoinSymbolApi = new KucoinSymbolApi();

    @PostMapping("/check-symbol")
    public ResponseEntity<SymbolCheckResponse> checkSymbol(@RequestBody SymbolCheckRequest request) {
        SymbolService symbolService = new SymbolService();
        boolean result = symbolService.symbolService(request.getExchange(), request.getSymbol());
        SymbolCheckResponse response = new SymbolCheckResponse(result);
        return ResponseEntity.ok(response);
    }
}
