package bybit.bybit_exel.dto;

import lombok.Data;

@Data
public class SymbolCheckResponse {
    private boolean ok;

    public SymbolCheckResponse(boolean exists) {
        this.ok = exists;
    }
}
