package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        log.info(LocalDateTime.now()
                + " Received GET request to /minprice endpoint with params: name=" + cryptoCurrency);
        return tradeService.findTradeWithMinPriceByCryptoCurrency(cryptoCurrency);
    }

    @GetMapping("/maxprice")
    public Trade getTradeWithMaxPriceByCryptoCurrency(@RequestParam(name = "name") String cryptoCurrency) {
        log.info(LocalDateTime.now()
                + " Received GET request to /maxprice endpoint with params: name=" + cryptoCurrency);
        return tradeService.findTradeWithMaxPriceByCryptoCurrency(cryptoCurrency);
    }

    @GetMapping
    public List<Trade> getTradesByCryptoCurrencyPage(
            @RequestParam(name = "name") String cryptoCurrency,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info(LocalDateTime.now()
                + " Received GET request to / endpoint with params: name=" + cryptoCurrency + " page=" +
                page + " size=" + size);
        return tradeService
                .findByCryptoCurrencyIgnoreCaseOrderByPrice(cryptoCurrency, PageRequest.of(page, size));
    }

    @GetMapping("/csv")
    public void generateCSVReport(HttpServletResponse httpServletResponse) {
        log.info(LocalDateTime.now()
                + " Received GET request to /csv endpoint");
        tradeService.generateCSVReport(httpServletResponse);
    }


}
