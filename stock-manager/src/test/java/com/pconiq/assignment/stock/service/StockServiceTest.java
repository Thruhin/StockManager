package com.pconiq.assignment.stock.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.pconiq.assignment.stock.config.InitialSetUpProcessor;
import com.pconiq.assignment.stock.exception.StockManagementException;
import com.pconiq.assignment.stock.model.restobjects.BaseResponse;
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

import lombok.SneakyThrows;

@SpringBootTest
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class , DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
@ExtendWith(DBUnitExtension.class)
public class StockServiceTest {
    
    @Autowired
    private DataSource dataSource;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    
    @Autowired
    StockService stockService;
    
    @MockBean
    StockRepository stockRepository;
    
    @MockBean
    InitialSetUpProcessor initProcessor;
    
    @MockBean
    StockPriceRepository stockPriceRepository;
    
    static Stock stock;
    
    @BeforeAll
    public static void setUp() {
        stock = new Stock();
        stock.setCompanyUrl("https://company-1" + ".com");
        stock.setCurrencyCode("EUR");
        stock.setCurrentPrice(StocksUtil.getRandomPrice());
        stock.setDescription("company 1 Stock details");
        stock.setLastUpdated(StocksUtil.getPreviousTimestampByDays(365));
        stock.setName("Company-1");
    }
    
    @Test
    public void testGetPagedStocks() {
        StockPrice price = new StockPrice();
        List<Stock> stockList = Arrays.asList(stock);
        Page<Stock> pagedStocks = new PageImpl<Stock>(stockList, PageRequest.of(1, 10), stockList.size());
        Mockito.when(stockRepository.findAll(Mockito.any( Pageable.class))).thenReturn(pagedStocks);
        Mockito.when(stockPriceRepository.saveAndFlush(Mockito.any(StockPrice.class))).thenReturn(price);

        // Execute the service call
        Page<Stock> returnedStocks = stockService.gePagedStocks(10,1);

        // Assert the response
        Assertions.assertNotNull(returnedStocks);
        Assertions.assertEquals(pagedStocks.getNumberOfElements(),returnedStocks.getNumberOfElements());
    }
    
    @Test
    public void testAllStocks() {
        StockPrice price = new StockPrice();
        List<Stock> stockList = Arrays.asList(stock);
        Mockito.when(stockRepository.findAll()).thenReturn(stockList);
        Mockito.when(stockPriceRepository.saveAndFlush(Mockito.any(StockPrice.class))).thenReturn(price);
        // Execute the service call
        StocksResponse stocksResponse = stockService.geStocks();

        // Assert the response
        Assertions.assertNotNull(stocksResponse.getStocks());
        Assertions.assertEquals(stocksResponse.getStocks().get(0).getStockId(),stock.getStockId());
    }
    
    @Test
    @SneakyThrows
    public void testStocksByIdSuccess() {
        
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stock));
        // Execute the service call
        StockDetailsResponse stocksDetailResponse = stockService.geStockById(Long.valueOf(1));

        // Assert the response
        Assertions.assertNotNull(stocksDetailResponse.getStock());
        Assertions.assertEquals(Constants.STOCK_FETCH_SUCCESS, stocksDetailResponse.getStatusCode());
    }
    
    @Test
    @SneakyThrows
    public void testStocksByIdInvalidId() {
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        // Execute the service call
        //StockDetailsResponse stocksDetailResponse = stockService.geStockById(Long.valueOf(1));
        // Assert the response
        StockManagementException exception = Assertions.assertThrows(StockManagementException.class, () -> {
            stockService.geStockById(Long.valueOf(1));
        });
        String expectedMessage = "Requested Stock not found";
        String actualMessage = exception.getMessage();
        
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
    

    @Test
    @SneakyThrows
    public void testGetStockPriceById() {
        StockPrice stockPrice = new StockPrice();
        stockPrice.setHistId(Long.valueOf(1));
        stockPrice.setCurrentPrice(1234);
        
        List<StockPrice> spList = Arrays.asList(stockPrice);
        Mockito.when(stockPriceRepository.getAllById(Mockito.anyLong())).thenReturn(spList);
        // Execute the service call
        StockPriceResponse stocksPriceResponse = stockService.geStockPriceById(Long.valueOf(1));

        // Assert the response
        Assertions.assertNotNull(stocksPriceResponse.getStockPrices());
        Assertions.assertEquals(Constants.STOCK_PRICE_HISTORY_FETCH_SUCCESS, stocksPriceResponse.getStatusCode());
    }
    
    
    @Test
    @SneakyThrows
    public void testCreateStock() {
        
        StockRequest request = new StockRequest();
        Mockito.when(stockRepository.saveAndFlush(Mockito.any(Stock.class))).thenReturn(stock);
        StockDetailsResponse response =  stockService.createStock(request);
        
        Assertions.assertNotNull(response.getStock());
        Assertions.assertEquals(Constants.STOCK_CREATE_SUCCESS, response.getStatusCode());
    }
    
    @Test
    @SneakyThrows
    public void testUpdateStockPrice() {
        
        StockPriceRequest request = new StockPriceRequest();
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stock));
        Mockito.when(stockRepository.updateStockPrice(Mockito.anyLong(), Mockito.anyFloat(), Mockito.anyString(), Mockito.any(Timestamp.class))).thenReturn(1);
        StockDetailsResponse response = stockService.updateStockPrice(request);
        Assertions.assertNotNull(response.getStock());
        Assertions.assertEquals(Constants.STOCK_UPDATE_SUCCESS, response.getStatusCode());
    }
    
    @Test
    @SneakyThrows
    public void testUpdateStockPriceInvalidId() {
        
        StockPriceRequest request = new StockPriceRequest();
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(stockRepository.updateStockPrice(Mockito.anyLong(), Mockito.anyFloat(), Mockito.anyString(), Mockito.any(Timestamp.class))).thenReturn(1);
        
        StockManagementException exception = Assertions.assertThrows(StockManagementException.class, () -> {
            stockService.updateStockPrice(request);
        });
        String expectedMessage = "Requested Stock not found";
        String actualMessage = exception.getMessage();
        
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
    }
    
    @Test
    @SneakyThrows
    public void testDeleteStockPrice() {
        
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stock));
        BaseResponse response = stockService.deleteStock(Long.valueOf(1));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.STOCK_DELETE_SUCCESS, response.getStatusCode());
    }
    
    @Test
    @SneakyThrows
    public void testDeleteStockPriceInvalidId() {
        
        Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        StockManagementException exception = Assertions.assertThrows(StockManagementException.class, () -> {
            stockService.deleteStock(Long.valueOf(1));
        });
        String expectedMessage = "Stock with given id is not available";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        
    }

}
