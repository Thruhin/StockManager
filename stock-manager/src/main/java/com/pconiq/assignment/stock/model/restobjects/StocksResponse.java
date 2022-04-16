package com.pconiq.assignment.stock.model.restobjects;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StocksResponse extends BaseResponse{

    /**
     * 
     */
    private static final long serialVersionUID = 205567826073453795L;

    private List<StockDetails> stocks;
    
}
