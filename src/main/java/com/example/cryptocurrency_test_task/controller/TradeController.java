package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.repository.TradeRepository;
import com.example.cryptocurrency_test_task.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cryptocurrencies/api/v1")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/minprice")
    public Trade getTradeWithMinPriceByCryptoCurrency(@RequestParam(name = "name") String cryptoCurrency) {
        return tradeService.findTradeWithMinPriceByCryptoCurrency(cryptoCurrency);
    }

    @GetMapping("/maxprice")
    public Trade getTradeWithMaxPriceByCryptoCurrency(@RequestParam(name = "name") String cryptoCurrency) {
        return tradeService.findTradeWithMaxPriceByCryptoCurrency(cryptoCurrency);
    }

    @GetMapping
    public List<Trade> getTradesByCryptoCurrencyPage(
            @RequestParam(name = "name") String cryptoCurrency,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return tradeService
                .findByCryptoCurrencyIgnoreCaseOrderByPrice(cryptoCurrency, PageRequest.of(page, size));
    }




}
