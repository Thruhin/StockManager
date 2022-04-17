package com.pconiq.assignment.stock.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeCurrencyConversionImpl implements CurrencyConversionService{

    
    @Override
    public float getConversionRateToBaseCurrency(String targetCurrency) {
        //TO-DO:
        //This can be enhanced to use 3rd party Services in order to convert the Currency from/to User preference
        return 1;
    }

}
