package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

@Slf4j
@RestController
@RequestMapping("/cryptocurrencies/api/v1")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeRepository) {
        this.tradeService = tradeRepository;
    }

    @GetMapping("/minprice")
    public Trade getMinPrice(@RequestParam(name = "name") String cryptoCurrency) {
        return tradeService.findTradeWithMinPriceByCryptoCurrency(cryptoCurrency);
    }

    @GetMapping("/maxprice")
    public Trade getMaxPrice(@RequestParam(name = "name") String cryptoCurrency) {
        return tradeService.findTradeWithMaxPriceByCryptoCurrency(cryptoCurrency);
    }


}
