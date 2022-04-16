package com.pconiq.assignment.stock.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import com.pconiq.assignment.stock.model.restobjects.StockDetails;
import com.pconiq.assignment.stock.repo.entity.Stock;

public class StocksUtil {
    
    static int min = 500;  
    static int max = 10000;
    static int[] arr = new int[] { -50, -40, -30, -20, -10, 10, 20, 30, 40, 50};
    
    /**
     * Returns default value for a Top and Skip
     * @param inputValue
     * @param defaultValue
     * @return
     */
    public static Integer getSkipOrTopValue(Integer inputValue, Integer defaultValue) {
        return (inputValue == null) ? defaultValue : inputValue;
      }
    
    /**
     * Converts a Stock entity to a rest bean
     * @param stock
     * @return
     */
    public static StockDetails convertStockToStockDetail(Stock stock) {
        StockDetails stockDetail = new StockDetails();
        stockDetail.setStockId(stock.getStockId());
        stockDetail.setCompanyUrl(stock.getCompanyUrl());
        stockDetail.setCreatedTime(stock.getCreatedTime());
        stockDetail.setCurrencyCode(stock.getCurrencyCode());
        stockDetail.setCurrentPrice(stock.getCurrentPrice());
        stockDetail.setCurrentPrice(stock.getCurrentPrice());
        stockDetail.setDescription(stock.getDescription());
        stockDetail.setName(stock.getName());
        stockDetail.setLastUpdate(stock.getLastUpdated());
        return stockDetail;
    }
    
    /**
     * Fetches the Timestamp by adding the provided Date
     * @param days number of days that should be added
     * @return Timestamp
     */
    public static Timestamp getPreviousTimestampByDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        return new Timestamp(cal.getTimeInMillis());
    }
    
    /**
     * Fetches the current Timestamp 
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Fetches the Timestamp by adding/subtracting random days from predefined array
     * @return Timestamp
     */
    public static Timestamp getRandomPastTimestamp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, arr[new Random().nextInt(arr.length)]);
        return new Timestamp(cal.getTimeInMillis());
    }
    
    /**
     * Fetches a random number between given max and min values
     * @return
     */
    public static float getRandomPrice() {
        return (float) (Math.random() * (max-min+1) + min);
    }

}
