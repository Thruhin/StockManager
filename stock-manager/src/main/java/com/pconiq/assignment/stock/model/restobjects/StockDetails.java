package com.pconiq.assignment.stock.model.restobjects;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockDetails{
    
    
    private Long stockId;
    private String name;
    private String description;
    private String companyUrl;
    private float currentPrice;
    private String currencyCode;
    private Timestamp createdTime;
    private Timestamp lastUpdate;
}
