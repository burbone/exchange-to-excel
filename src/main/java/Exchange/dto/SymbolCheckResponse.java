package Exchange.dto;

import lombok.Data;

@Data
public class SymbolCheckResponse {
    private boolean ok;

    public SymbolCheckResponse(boolean exists) {
        this.ok = exists;
    }
}
