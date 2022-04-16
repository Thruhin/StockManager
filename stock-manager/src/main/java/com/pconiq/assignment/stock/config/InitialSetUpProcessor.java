package com.pconiq.assignment.stock.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.pconiq.assignment.stock.EnvVariables;
import com.pconiq.assignment.stock.model.restobjects.StockDetails;
import com.pconiq.assignment.stock.model.restobjects.StockPriceRequest;
import com.pconiq.assignment.stock.model.restobjects.StockRequest;
import com.pconiq.assignment.stock.service.StockService;
import com.pconiq.assignment.stock.util.StocksUtil;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Configuration
public class InitialSetUpProcessor {

    Logger logger = LoggerFactory.getLogger(InitialSetUpProcessor.class.getName());

    String liquibaseChangeLogMaster = "/db/master-onboard.xml";

    @Autowired
    private StockService stockService;
    
    @Autowired
    private EnvVariables envVariables;

    @Autowired
    private LiquibaseConfigService liquibaseConfigService;
    
    /**
     * This method takes care of executing DB Scripts on the Schema and inserting default Stock records
     * @param event Spring context event
     * @throws Exception
     */

    @EventListener
    @Async
    public void onApplicationEvent(ContextRefreshedEvent event) throws Exception {

        executeInitialSetupScripts();
        updateSequenceWithLatestStockId();
        if(envVariables.isExecuteInitialScripts()) {
            //Executors.newCachedThreadPool().submit(() -> {
                logger.info("Starting initial stock data creation in a separate thread");
                initialStockDataInsert();
                logger.info("Completed initial stock data creation");
          //  });
        }

    }

    /**
     * This method resets the Sequence with next value of Stock Id sequence
     */
    private void updateSequenceWithLatestStockId() {
        Long stockId = (long) 0;
        String sql = "ALTER SEQUENCE stock_seq RESTART WITH " + stockId + ";";
        try(Connection connection = liquibaseConfigService.createPostgresDataSource().getConnection();
            Statement statement = connection.createStatement();PreparedStatement ps = connection.prepareStatement(sql);) {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("select MAX(STOCK_ID) as NEXT_SEQ_ID from STOCK");
            if (rs.next()) {
                stockId = rs.getLong("NEXT_SEQ_ID") + 1;
            }
            ps.executeUpdate();
            logger.info("Sequence is reset with latest value");
        } catch (SQLException e) {
            logger.error("Unable to reet the sequence with exception {}", e.getMessage());
        } 
    }

    /**
     * Reads from configurations if data should be inserted and persists the stocks
     * Also updates the Stock multiple times with random prices
     */
    @Transactional
    private void initialStockDataInsert() {
        logger.info("Starting creation and updation of {} records of stock data", envVariables.getStockSize());
        for (int seq = 1; seq <= envVariables.getStockSize(); seq++) {
            StockDetails stockDetails = createStock(seq);
            stockService.updateStockPrice(new StockPriceRequest(stockDetails.getStockId(), StocksUtil.getRandomPrice(),
                "EUR", StocksUtil.getRandomPastTimestamp()));
            stockService.updateStockPrice(new StockPriceRequest(stockDetails.getStockId(), StocksUtil.getRandomPrice(),
                "EUR", StocksUtil.getRandomPastTimestamp()));
            stockService.updateStockPrice(new StockPriceRequest(stockDetails.getStockId(), StocksUtil.getRandomPrice(),
                "EUR", StocksUtil.getRandomPastTimestamp()));
            stockService.updateStockPrice(new StockPriceRequest(stockDetails.getStockId(), StocksUtil.getRandomPrice(),
                "EUR", StocksUtil.getRandomPastTimestamp()));
            stockService.updateStockPrice(new StockPriceRequest(stockDetails.getStockId(), StocksUtil.getRandomPrice(),
                "EUR", StocksUtil.getRandomPastTimestamp()));
        }
        logger.info("Completed creation and updation of {} stock ", envVariables.getStockSize());
    }

    /**
     * This method creates a stock by initializing the stock using index
     * @param seq index of the Stock that should be created
     * @return Stock details of created Stock
     */
    private StockDetails createStock(int seq) {
        StockRequest stockRequest = new StockRequest();
        stockRequest.setCompanyUrl("https://company-" + seq + ".com");
        stockRequest.setCurrencyCode("EUR");
        stockRequest.setCurrentPrice(StocksUtil.getRandomPrice());
        stockRequest.setDescription("company" + seq + " Stock details");
        stockRequest.setLastUpdated(StocksUtil.getPreviousTimestampByDays(365));
        stockRequest.setName("Company-" + seq);
        return stockService.createStock(stockRequest).getStock();
    }

    /**
     * This method connects to the Database configured and executes DDL and DML scripts using liquibase
     * @throws Exception
     */
    private void executeInitialSetupScripts() throws Exception {

        // Release Liquibase locks on the schema if any
        // liquibaseConfigService.releaseLock(cacheDataSource.getDefaultCacheDatasource(), schema, schema);
        logger.info("Executing Liquibase scripts");

        // Connection dbConnection = liquibaseConfigService.getGlobalPostgresDataSource().getConnection();
        Connection dbConnection = liquibaseConfigService.createPostgresDataSource().getConnection();

        if (dbConnection == null) {
            logger.error("Could not get valid DB connection while executing Liquibase scripts");
            throw new Exception("runLiquibaseOnTenantDatabase: database connection is null for tenant:");
        }
        Database database = getDatabase(dbConnection);
        Liquibase liquibase = getLiquibase(database);
        try {
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException ex) {
            logger.error("Exception while executing Liquibase scripts with message {}", ex.getMessage());
            throw ex;
        }
        logger.info("Completed executing Liquibase scripts");

    }

    /**
     * Created a new instance of Liquibase for creation of Scripts
     * @param database
     * @return
     */
    Liquibase getLiquibase(Database database) {
        try {
            logger.debug("getLiquibase: using changelog file [{}]" + liquibaseChangeLogMaster);
            return new Liquibase(liquibaseChangeLogMaster, new ClassLoaderResourceAccessor(), database);
        } catch (Exception ex) {
            logger.debug("Could not create Liquibase instance. Exception is  : {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Fetches Database instance
     * @param dbConnection
     * @return Database instance of DB
     * @throws Exception
     */
    Database getDatabase(Connection dbConnection) throws Exception {
        try {
            return getDatabaseFactory().findCorrectDatabaseImplementation(new JdbcConnection(dbConnection));
        } catch (DatabaseException ex) {
            logger.error("Could not get database instance for the available connection");
            return null;
        }
    }

    DatabaseFactory getDatabaseFactory() {
        return DatabaseFactory.getInstance();
    }

}
