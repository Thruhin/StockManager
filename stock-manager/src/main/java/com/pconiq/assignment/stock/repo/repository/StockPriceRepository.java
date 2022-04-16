package com.pconiq.assignment.stock.repo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pconiq.assignment.stock.repo.entity.StockPrice;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long>{

    List<StockPrice> findAllByStockId(Long stockId);
    
    @Query(value = "SELECT * FROM STOCK_PRICE_HISTORY WHERE STOCK_ID = :stockId", nativeQuery = true)
    public List<StockPrice> getAllById(@Param("stockId") Long stockId);

}
