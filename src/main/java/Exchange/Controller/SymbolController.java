package Exchange.Controller;

import Exchange.api.BybitApi.BybitSymbolApi;
import Exchange.api.KucoinApi.KucoinSymbolApi;
import Exchange.dto.SymbolCheckRequest;
import Exchange.dto.SymbolCheckResponse;
import Exchange.service.SymbolService;
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
