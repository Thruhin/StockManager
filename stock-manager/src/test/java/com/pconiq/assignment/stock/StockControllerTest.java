package com.pconiq.assignment.stock;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.pconiq.assignment.stock.config.HibernateConfig;
import com.pconiq.assignment.stock.config.InitialSetUpProcessor;
import com.pconiq.assignment.stock.model.restobjects.BaseResponse;
import com.pconiq.assignment.stock.model.restobjects.StockDetails;
import com.pconiq.assignment.stock.model.restobjects.StockDetailsResponse;
import com.pconiq.assignment.stock.model.restobjects.StockPriceRequest;
import com.pconiq.assignment.stock.model.restobjects.StockRequest;
import com.pconiq.assignment.stock.repo.entity.Stock;
import com.pconiq.assignment.stock.service.StockService;
import com.pconiq.assignment.stock.util.Constants;
import com.pconiq.assignment.stock.util.StocksUtil;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class , DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
@ExtendWith(DBUnitExtension.class)
public class StockControllerTest {
    
    @Autowired
    private DataSource dataSource;


    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @MockBean
    StockService stockService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InitialSetUpProcessor initProcessor;
    
    static Stock stock;
    
    private final ObjectMapper mapper = new ObjectMapper();
    
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
    public void testfindAllStockswithPagination() throws Exception {
        List<Stock> stockList = Arrays.asList(stock);
        Page<Stock> pagedStocks = new PageImpl<Stock>(stockList, PageRequest.of(1, 10), stockList.size());

        Mockito.when(stockService.gePagedStocks(Mockito.anyInt(), Mockito.anyInt())).thenReturn(pagedStocks);
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testfindByStockId() throws Exception {

        StockDetails stockDetails = StocksUtil.convertStockToStockDetail(stock);
        StockDetailsResponse response = new StockDetailsResponse();
        response.setStock(stockDetails);

        Mockito.when(stockService.geStockById(Mockito.anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/stocks/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.jsonPath("$.stock").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.stock.name").isString())
            .andExpect(MockMvcResultMatchers.jsonPath("$.stock.name").value("Company-1"));
    }
    
    @Test
    @SneakyThrows
    public void testCreateStock() {
        
        StockDetails stockDetails = StocksUtil.convertStockToStockDetail(stock);
        StockDetailsResponse response = new StockDetailsResponse();
        response.setStock(stockDetails);
        
        StockRequest stockRequest = new StockRequest();
        stockRequest.setCompanyUrl("https://company-" + 1 + ".com");
        stockRequest.setCurrencyCode("EUR");
        stockRequest.setCurrentPrice(StocksUtil.getRandomPrice());
        stockRequest.setDescription("company" + 1 + " Stock details");
        stockRequest.setLastUpdated(StocksUtil.getPreviousTimestampByDays(365));
        stockRequest.setName("Company-" + 1);
        
        Mockito.when(stockService.createStock(Mockito.any(StockRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/stock")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(stockRequest)))
            // Validate the response code and content type
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            // Validate the returned fields
            .andExpect(jsonPath("$.stock.name", is("Company-1")))
            .andExpect(jsonPath("$.stock.companyUrl", is("https://company-1.com")));
        
    }
    
    @Test
    @SneakyThrows
    public void testUpdateStock() {
        
        StockDetails stockDetails = StocksUtil.convertStockToStockDetail(stock);
        StockDetailsResponse response = new StockDetailsResponse();
        response.setStock(stockDetails);
        
        StockPriceRequest stockPriceRequest = new StockPriceRequest();
        stockPriceRequest.setStockId(1);
        stockPriceRequest.setCurrencyCode("EUR");
        stockPriceRequest.setCurrentPrice(StocksUtil.getRandomPrice());
        
        Mockito.when(stockService.updateStockPrice(Mockito.any(StockPriceRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.patch("/stock")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(stockPriceRequest)))
            // Validate the response code and content type
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            // Validate the returned fields
            .andExpect(jsonPath("$.stock.name", is("Company-1")))
            .andExpect(jsonPath("$.stock.companyUrl", is("https://company-1.com")));
        
    }
    
    @Test
    @SneakyThrows
    public void testDeleteStock() {
        
        BaseResponse response = new BaseResponse();
        response.setStatusCode(Constants.STOCK_DELETE_SUCCESS);
        
        Mockito.when(stockService.deleteStock(Mockito.anyLong())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.delete("/stock/1")
            .contentType(MediaType.APPLICATION_JSON))
            // Validate the response code and content type
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode",is(Constants.STOCK_DELETE_SUCCESS)));
        
    }
    
}
