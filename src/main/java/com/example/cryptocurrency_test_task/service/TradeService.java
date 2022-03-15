package com.example.cryptocurrency_test_task.service;

import com.example.cryptocurrency_test_task.domain.Trade;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeService {
    Trade findTradeWithMinPriceByCryptoCurrency(String cryptoCurrency);
    Trade findTradeWithMaxPriceByCryptoCurrency(String cryptoCurrency);
    List<Trade> findByCryptoCurrencyIgnoreCaseOrderByPrice(String cryptoCurrency, Pageable pageable);
}
