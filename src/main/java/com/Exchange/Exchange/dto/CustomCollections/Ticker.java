package com.Exchange.Exchange.dto.CustomCollections;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Ticker {
    private String exchange;
    private Double bidPrice;
    private Double askPrice;

    @Override
    public int hashCode(){
        return Objects.hash(exchange, bidPrice, askPrice);
    }

}
