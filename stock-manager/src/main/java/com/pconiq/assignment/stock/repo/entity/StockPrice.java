package com.pconiq.assignment.stock.repo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.pconiq.assignment.stock.enums.Operation;
import com.pconiq.assignment.stock.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = Constants.TABLE_STOCK_PRICE_HISTORY)
public class StockPrice implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1446445210686902161L;
    
    
    @Id
    @Column(name = "HIST_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_price_history_generator")
    @SequenceGenerator(name = "stock_price_history_generator", sequenceName = "STOCK_PRICE_HISTORY_SEQ", allocationSize = 1)
    private Long histId;
    
    @Column(name = "STOCK_ID")
    private Long stockId;
    
    @Column(name = "CURRENT_PRICE")
    private float currentPrice;
    
    @Column(name = "LAST_UPDATED_TS")
    private Timestamp lastUpdated;
    
    @Column(name = "CHANGE_IN_PRICE")
    private float changeInPrice;
    
    @Column(name = "OPERATION")
    private Operation operation;
    
    /*@ManyToOne
    @JoinColumn(name = "ID", nullable = false, insertable = false, updatable = false)
    private Stock stock;     */
    
    //@ManyToOne
    //@JoinColumn(name="STOCK_ID", insertable = false, nullable=false, updatable = false)
    //public Stock stock;

}
