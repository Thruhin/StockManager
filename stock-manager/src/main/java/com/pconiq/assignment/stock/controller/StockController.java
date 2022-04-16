package com.pconiq.assignment.stock.controller;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pconiq.assignment.stock.exception.StockManagementException;
import com.pconiq.assignment.stock.model.restobjects.BaseResponse;
import com.pconiq.assignment.stock.model.restobjects.StockDetailsResponse;
import com.pconiq.assignment.stock.model.restobjects.StockPriceRequest;
import com.pconiq.assignment.stock.model.restobjects.StockPriceResponse;
import com.pconiq.assignment.stock.model.restobjects.StockRequest;
import com.pconiq.assignment.stock.model.restobjects.StocksResponse;
import com.pconiq.assignment.stock.repo.entity.Stock;
import com.pconiq.assignment.stock.service.StockService;
import com.pconiq.assignment.stock.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Validated
public class StockController {

    org.slf4j.Logger logger = LoggerFactory.getLogger(StockController.class.getName());

    @Autowired
    private StockService stockService;

    @Operation(summary = "Get all the Stocks. This method doesnt expose")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Stock details are fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to get the stocks") })
    @GetMapping(value = "/stocks/all")
    public ResponseEntity<StocksResponse> getStocks() {
        StocksResponse response = new StocksResponse();
        ResponseEntity<StocksResponse> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(stockService.geStocks(), HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Failed to get Stocks with exception : {}", exception.getMessage());
            response.setStatusCode(Constants.STOCKS_FETCH_FAILED);
            response.setStatusMessage("Failed to get stocks");
            responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Get all the Stocks with Pagination")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Stock details are fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to get the stocks") })
    @GetMapping(value = "/stocks")
    public Page<Stock> getPagedStocks(@RequestParam(value = "skip", required = false) Integer skip,
        @RequestParam(value = "top", required = false) Integer top) {
        logger.info("Request for Stocks with pagination having values skip {} and top {}");
        Page<Stock> response = stockService.gePagedStocks(top, skip);
        logger.info("Completed request for Stocks with pagination having values skip {} and top {}");
        return response;
    }

    @Operation(summary = "Get Stock details for provided Stock Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Stock details are fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to get the stock detail for given stock iD") })
    @GetMapping(value = "/stocks/{stockId}")
    public ResponseEntity<StockDetailsResponse> getStock(@PathVariable Long stockId) {
        StockDetailsResponse response = new StockDetailsResponse();
        ResponseEntity<StockDetailsResponse> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(stockService.geStockById(stockId), HttpStatus.OK);
        } catch (StockManagementException exception) {
            logger.error("Failed to get Stock with exception : {}", exception.getMessage());
            response.setStatusCode(Constants.STOCK_WITH_ID_FETCH_FAILED);
            response.setStatusMessage("Failed to get stock with id " + stockId);
            responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            logger.error("Failed to get Stocks with exception : {}", exception.getMessage());
            response.setStatusCode(Constants.STOCK_WITH_ID_FETCH_FAILED_WITH_EXCEPTION);
            response.setStatusMessage("Failed to get stocks");
            responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Get history information of a provided Stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock price history details are fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to get price history of Stock") })
    @GetMapping(value = "/stocks/{stockId}/history")
    public ResponseEntity<StockPriceResponse> getStockPriceHistory(@PathVariable Long stockId) {

        StockPriceResponse response = new StockPriceResponse();
        ResponseEntity<StockPriceResponse> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(stockService.geStockPriceById(stockId), HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Failed to get Stocks price history with exception : {}", exception.getMessage());
            response.setStatusCode(Constants.STOCK_PRICE_HISTORY_FAILED);
            response.setStatusMessage("Failed to get Stocks price history");
            responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
        
    }

    @Operation(summary = "Create a new Stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock information is created successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to create a new Stock") })
    @PostMapping(value = "/stock")
    public ResponseEntity<StockDetailsResponse> createStock(@Valid @RequestBody StockRequest stockRequest) {
        StockDetailsResponse response = new StockDetailsResponse();
        ResponseEntity<StockDetailsResponse> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(stockService.createStock(stockRequest), HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Failed to create Stocks with exception : {}", exception.getMessage());
            response.setStatusCode(Constants.STOCK_CREATE_FAILED);
            response.setStatusMessage("Failed to create a new stock");
            responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
        
    }

    @Operation(summary = "Update an existing Stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock price information is updated successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to create a new Stock") })
    @PatchMapping(value = "/stock")
    public ResponseEntity<StockDetailsResponse> updateStockPrice(@Valid @RequestBody StockPriceRequest stockPriceRequest) {
        StockDetailsResponse response = new StockDetailsResponse();
        ResponseEntity<StockDetailsResponse> repsonseEntity = null;
        try {
            repsonseEntity = new ResponseEntity<>(stockService.updateStockPrice(stockPriceRequest), HttpStatus.OK);
        } catch (Exception exception) {
            response.setStatusCode(Constants.STOCK_UPDATE_FAILED);
            response.setStatusMessage("Failed to update the Stock price");
            repsonseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return repsonseEntity;
    }

    @Operation(summary = "Delete an existing Stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock price information is deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "400", description = "Bad request. Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to delete a.exosting Stock") })
    @DeleteMapping(value = "/stock/{stockId}")
    public ResponseEntity<BaseResponse>  deleteStock(@PathVariable Long stockId) {
        BaseResponse response = new BaseResponse();
        ResponseEntity<BaseResponse> repsonseEntity = null;
        try {
            repsonseEntity = new ResponseEntity<>(stockService.deleteStock(stockId), HttpStatus.OK);
        } catch (Exception exception) {
            response.setStatusCode(Constants.STOCK_UPDATE_FAILED);
            response.setStatusMessage("Failed to delete the Stock");
            repsonseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return repsonseEntity;
    }

}
