package com.example.cryptocurrency_test_task.service;

import com.example.cryptocurrency_test_task.domain.Trade;

public interface TradeService {
    Trade findTradeWithMinPriceByCryptoCurrency(String cryptoCurrency);
    Trade findTradeWithMaxPriceByCryptoCurrency(String cryptoCurrency);
}
