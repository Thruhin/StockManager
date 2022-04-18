package com.pconiq.assignment.stock.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pconiq.assignment.stock.enums.Operation;
import com.pconiq.assignment.stock.exception.StockManagementException;
import com.pconiq.assignment.stock.model.restobjects.BaseResponse;
import com.pconiq.assignment.stock.model.restobjects.StockDetails;
import com.pconiq.assignment.stock.model.restobjects.StockDetailsResponse;
import com.pconiq.assignment.stock.model.restobjects.StockPriceRequest;
import com.pconiq.assignment.stock.model.restobjects.StockPriceResponse;
import com.pconiq.assignment.stock.model.restobjects.StockRequest;
import com.pconiq.assignment.stock.model.restobjects.StocksResponse;
import com.pconiq.assignment.stock.repo.entity.Stock;
import com.pconiq.assignment.stock.repo.entity.StockPrice;
import com.pconiq.assignment.stock.repo.repository.StockPriceRepository;
import com.pconiq.assignment.stock.repo.repository.StockRepository;
import com.pconiq.assignment.stock.util.Constants;
import com.pconiq.assignment.stock.util.StocksUtil;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private CurrencyConversionService currencyService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceRepository stockPriceRepository;

    public StocksResponse geStocks() {

        List<Stock> stocks = stockRepository.findAll();
        List<StockDetails> stockDetails = new ArrayList<>();
        stocks.forEach((stock) -> {
            StockDetails stockDetail = StocksUtil.convertStockToStockDetail(stock);
            stockDetails.add(stockDetail);
        });
        StocksResponse response = new StocksResponse();
        response.setStocks(stockDetails);
        response.setStatusCode(Constants.STOCKS_FETCH_SUCCESS);
        response.setStatusMessage("Stocks retreived succesfully");
        return response;
    }

    public Page<Stock> gePagedStocks(Integer top, Integer skip) {
        skip = StocksUtil.getSkipOrTopValue(skip, Constants.DEFAULT_SKIP_VALUE);
        top = StocksUtil.getSkipOrTopValue(top, Constants.DEFAULT_TOP_VALUE);
        top = (top > Constants.DEFAULT_TOP_VALUE) ? Constants.DEFAULT_TOP_VALUE : top;
        Page<Stock> pagedStocks = stockRepository.findAll(PageRequest.of((skip / top), top));
        return pagedStocks;
    }

    public StockDetailsResponse geStockById(Long stockId) throws StockManagementException {
        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (stockOptional.isEmpty()) {
            throw new StockManagementException(Constants.STOCKS_FETCH_FAILED, "Requested Stock not found.",
                HttpStatus.BAD_REQUEST);
        }
        Stock stock = stockOptional.get();
        StockDetailsResponse response = new StockDetailsResponse();
        response.setStatusCode(Constants.STOCK_FETCH_SUCCESS);
        response.setStatusMessage("Retrieed Stock details sucessfully");
        response.setStock(StocksUtil.convertStockToStockDetail(stock));
        return response;

    }

    public StockPriceResponse geStockPriceById(Long stockId) {
        StockPriceResponse response = new StockPriceResponse();
        response.setStatusCode(Constants.STOCK_PRICE_HISTORY_FETCH_SUCCESS);
        response.setStatusMessage("Fetched the Stock price history Successfully");
        response.setStockPrices(stockPriceRepository.getAllById(stockId));
        return response;

    }

    @Transactional
    public StockDetailsResponse createStock(StockRequest stockRequest) {
        Stock stock = new Stock();
        stock.setCompanyUrl(stockRequest.getCompanyUrl());
        stock.setCurrencyCode(stockRequest.getCurrencyCode());
        stock.setCurrentPrice(stockRequest.getCurrentPrice()
            * currencyService.getConversionRateToBaseCurrency(stockRequest.getCurrencyCode()));
        stock.setName(stockRequest.getName());
        stock.setDescription(stockRequest.getDescription());
        stock.setCreatedTime(StocksUtil.getCurrentTimestamp());
        stock.setLastUpdated(
            stockRequest.getLastUpdated() == null ? StocksUtil.getCurrentTimestamp() : stockRequest.getLastUpdated());
        stock = stockRepository.saveAndFlush(stock);
        saveHistory(stock, Operation.Create, 0);
        StockDetailsResponse response = new StockDetailsResponse();
        response.setStatusCode(Constants.STOCK_CREATE_SUCCESS);
        response.setStatusMessage("Stock created successfully");
        response.setStock(StocksUtil.convertStockToStockDetail(stock));
        return response;
    }

    @Transactional
    public StockDetailsResponse updateStockPrice(StockPriceRequest stockPriceRequest) throws StockManagementException {
        Optional<Stock> stockOptional = stockRepository.findById(stockPriceRequest.getStockId());
        if (stockOptional.isEmpty()) {
            throw new StockManagementException(Constants.STOCK_UPDATE_FAILED, "Requested Stock not found.",
                HttpStatus.BAD_REQUEST);
        }

        Stock stock = stockOptional.get();
        // In order to allow Users to update with custom date
        Timestamp tz = stockPriceRequest.getLastUpdated() == null ? StocksUtil.getCurrentTimestamp()
            : stockPriceRequest.getLastUpdated();
        stock.setLastUpdated(tz);
        float oldPrice = stock.getCurrentPrice();
        stock.setCurrentPrice(stockPriceRequest.getCurrentPrice()
            * currencyService.getConversionRateToBaseCurrency(stockPriceRequest.getCurrencyCode()));
        stock.setCurrencyCode(stockPriceRequest.getCurrencyCode());
        stockRepository.updateStockPrice(stockPriceRequest.getStockId(), stockPriceRequest.getCurrentPrice(),
            stockPriceRequest.getCurrencyCode(), tz);
        saveHistory(stock, Operation.Update, oldPrice);
        StockDetailsResponse response = new StockDetailsResponse();
        response.setStatusCode(Constants.STOCK_UPDATE_SUCCESS);
        response.setStatusMessage("Stock updated successfully");
        response.setStock(StocksUtil.convertStockToStockDetail(stock));
        return response;
    }

    @Transactional
    public BaseResponse deleteStock(Long stockId) throws StockManagementException {
        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (stockOptional.isEmpty()) {
            throw new StockManagementException("Stock with given id is not available");
        }
        Stock stock = stockOptional.get();
        stockRepository.delete(stock);
        saveHistory(stock, Operation.Delete, stock.getCurrentPrice());
        BaseResponse response = new BaseResponse();
        response.setStatusCode(Constants.STOCK_DELETE_SUCCESS);
        response.setStatusMessage("Deleted the stock succesfully");

        return response;
    }

    private void saveHistory(Stock stock, Operation operation, float oldPrice) {
        StockPrice stockPrice = new StockPrice();
        stockPrice.setCurrentPrice(stock.getCurrentPrice());
        stockPrice.setOperation(operation);
        stockPrice.setStockId(stock.getStockId());
        stockPrice.setChangeInPrice( stock.getCurrentPrice() - oldPrice);
        stockPrice.setLastUpdated(StocksUtil.getCurrentTimestamp());
        stockPriceRepository.saveAndFlush(stockPrice);
    }

}
