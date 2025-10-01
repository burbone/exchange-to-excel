package com.Exchange.Exchange.controller.data;

import com.Exchange.Exchange.dto.Request.SymbolCheck_Request;
import com.Exchange.Exchange.dto.Response.SymbolCheck_Response;
import com.Exchange.Exchange.service.data.SymbolFullCheck;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SymbolCheck_Controller {

    /*
    что бы добавить новую биржу
    1 - добавить биржу в SymbolRemodeling
    2 - добавить биржу в SymbolFullCheck
    3 - добавить апи по символу в /api/биржа/SymbolБиржа_api
    4 - добавить биржу в data.html
    5 - добавить биржу в js файле
     */

    @PostMapping("/symbolcheck")
    public ResponseEntity<SymbolCheck_Response> symbolCheck(@RequestBody SymbolCheck_Request request) throws IOException, InterruptedException {
        String exchange = request.getExchange();
        String symbol = request.getSymbol();
        SymbolFullCheck symbolFullCheck = new SymbolFullCheck();

        boolean isValid = symbolFullCheck.fullCheck(exchange, symbol);

        SymbolCheck_Response response = new SymbolCheck_Response(isValid);
        return ResponseEntity.ok(response);
    }
}
