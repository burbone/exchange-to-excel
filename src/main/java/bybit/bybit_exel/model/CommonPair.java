package bybit.bybit_exel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPair {
    private String exchange1Pair;
    private String exchange2Pair;
    private String exchange1Price;
    private String exchange2Price;
} 