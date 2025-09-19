package Exchange.dto;

import lombok.Data;

@Data
public class SymbolCheckRequest {
    private String exchange;
    private String symbol;
}
