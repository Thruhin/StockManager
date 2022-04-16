package com.pconiq.assignment.stock.model.restobjects;

import java.sql.Timestamp;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.pconiq.assignment.stock.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequest {
    
    @Size(max = 128)
    @Pattern(regexp = Constants.REGEX_VALID_NAME)
    private String name;
    @Size(max = 1000)
    @Pattern(regexp = Constants.DESC_REGEX)
    private String description;
    @Size(max = 1000)
    @Pattern(regexp = Constants.REGEX_VALID_CHARS_URL)
    private String companyUrl;
    private float currentPrice;
    private String currencyCode;
    private Timestamp lastUpdated;

}
