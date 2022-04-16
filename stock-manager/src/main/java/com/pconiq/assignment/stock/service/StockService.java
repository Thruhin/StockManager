package com.pconiq.assignment.stock.service;

import org.springframework.data.domain.Page;

import com.pconiq.assignment.stock.exception.StockManagementException;
import com.pconiq.assignment.stock.model.restobjects.BaseResponse;
import com.pconiq.assignment.stock.model.restobjects.StockDetailsResponse;
import com.pconiq.assignment.stock.model.restobjects.StockPriceRequest;
import com.pconiq.assignment.stock.model.restobjects.StockPriceResponse;
import com.pconiq.assignment.stock.model.restobjects.StockRequest;
import com.pconiq.assignment.stock.model.restobjects.StocksResponse;
import com.pconiq.assignment.stock.repo.entity.Stock;

public interface StockService {
    
    /**
     * Method to return all the available stocks with no pagination
     * @return StocksResponse
     */
    public StocksResponse geStocks();
    
    /**
     * Method to return the requested stocks with pagination information
     * @param top size of records requested
     * @param skip records to be skipped
     * @return Page<Stock> returns metadata info of Page/records along with Stocks information
     */
    public Page<Stock> gePagedStocks(Integer top, Integer skip);
    
    /**
     * Returns a Stock based on the provided Stock Id
     * @param id unique identifier of the stock
     * @return Stock details
     * @throws StockManagementException when the Stock is not available 
     */
    public StockDetailsResponse geStockById(Long id) throws StockManagementException;
    
    /**
     * Returns a Stock Price history of a Stock based on the provided Id
     * @param stockId unique identifier of the stock
     * @return StockPriceResponse History of Price changes of the Stock
     */
    public StockPriceResponse geStockPriceById(Long stockId);

    /**
     * Creates a new Stock with next Sequence number
     * @param stockRequest
     * @return StockDetailsResponse Info about created Stock
     */
    public StockDetailsResponse createStock(StockRequest stockRequest);
    
    /**
     * Updates an existing Stock with price and currency code
     * @param stockRequest
     * @return StockDetailsResponse Info about updated Stock
     */
    public StockDetailsResponse updateStockPrice(StockPriceRequest stockPriceRequest);
    
    /**
     * Deleted an existing Stock based on the Id
     * @param stockId stock identifier that should be deleted
     * @return BaseResponse Info about the request status
     */
    public BaseResponse deleteStock(Long stockId) throws StockManagementException;
    
}
