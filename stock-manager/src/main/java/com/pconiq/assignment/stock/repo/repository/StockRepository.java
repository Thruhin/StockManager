package com.pconiq.assignment.stock.repo.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pconiq.assignment.stock.repo.entity.Stock;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
    
    @Query(value = "SELECT * FROM STOCK LIMIT :top OFFSET :skip",
        nativeQuery = true)
    public List<Stock> getAllStocks(@Param("top") int top, @Param("skip") int skip);
    
    public Page<Stock> findAll(Pageable pageable);
    
    @Modifying
    @Query(value = "update STOCK set CURRENT_PRICE = :currentPrice , CURRENCY_CODE = :currencyType, LAST_UPDATED_TS = :timestamp  where STOCK_ID = :stockId",
        nativeQuery = true)
    public int updateStockPrice(@Param("stockId") long stockId, @Param("currentPrice") float currentPrice, @Param("currencyType") String currencyType, @Param("timestamp") Timestamp lastUpdated);
    
    /*@Query(value = "SELECT MAX(STOCK_ID) from STOCK")
    public Long getCurrentHighestStockId();
    
    @Modifying
    @Query(value = "ALTER SEQUENCE STOCK_SEQ RESTART WITH :stockId", nativeQuery = true)
    public void alterSequenceWithHighestId(@Param("stockId") Long stockId); */
    
}
