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

import com.pconiq.assignment.stock.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = Constants.TABLE_STOCK)
public class Stock implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_generator")
    @SequenceGenerator(name = "stock_generator", sequenceName = "STOCK_SEQ", allocationSize = 1)
    @Column(name = "STOCK_ID")
    private Long stockId;
    
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "COMPANY_URL")
    private String companyUrl;
    
    @Column(name = "CURRENT_PRICE")
    private float currentPrice;
    
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;
    
    @Column(name = "CREATED_TS")
    private Timestamp createdTime;
    
    @Column(name = "LAST_UPDATED_TS")
    private Timestamp lastUpdated;
    
    /*@OneToMany(orphanRemoval = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID")
    @JsonIgnore
    private Set<StockPrice> stockPrices; */
    //, orphanRemoval = false, fetch = FetchType.LAZY
    //@OneToMany(mappedBy="stock", fetch = FetchType.LAZY)
    //private List<StockPrice> stockPrices;
    
}
