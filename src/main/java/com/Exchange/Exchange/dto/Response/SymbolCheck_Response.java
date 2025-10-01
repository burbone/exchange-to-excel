package com.Exchange.Exchange.dto.Response;

import lombok.Data;

@Data
public class SymbolCheck_Response {
    private boolean ok;

    public SymbolCheck_Response(boolean exists) {
        this.ok = exists;
    }
}
