package com.pconiq.assignment.stock.model.restobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceRequest {
    
    @Size(max = 32)
    private long stockId;
    private float currentPrice;
    private String currencyCode;
    private Timestamp lastUpdated;

}
