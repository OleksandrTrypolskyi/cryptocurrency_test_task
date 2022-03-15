package com.example.cryptocurrency_test_task.service;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;

    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Trade findTradeWithMinPriceByCryptoCurrency(String cryptoCurrency) {
        return tradeRepository
                .findTopByCryptoCurrencyOrderByPrice(cryptoCurrency)
                .orElseThrow();
    }

    @Override
    public Trade findTradeWithMaxPriceByCryptoCurrency(String cryptoCurrency) {
        return tradeRepository
                .findTopByCryptoCurrencyOrderByPriceDesc(cryptoCurrency)
                .orElseThrow();
    }
}
