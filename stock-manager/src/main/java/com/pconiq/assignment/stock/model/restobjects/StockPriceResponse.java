package com.pconiq.assignment.stock.model.restobjects;

import java.util.List;

import com.pconiq.assignment.stock.repo.entity.StockPrice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockPriceResponse extends BaseResponse{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private List<StockPrice> stockPrices;
    
    

}
