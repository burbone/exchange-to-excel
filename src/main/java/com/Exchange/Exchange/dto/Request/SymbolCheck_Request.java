package com.Exchange.Exchange.dto.Request;

import lombok.Data;

@Data
public class SymbolCheck_Request {
    private String exchange;
    private String symbol;
}
