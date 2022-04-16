package com.pconiq.assignment.stock.model.restobjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDetailsResponse extends BaseResponse{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private StockDetails stock;

}
