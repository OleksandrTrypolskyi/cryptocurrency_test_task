package com.example.cryptocurrency_test_task.bootstrap;

import com.example.cryptocurrency_test_task.domain.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class BootStrap implements ApplicationListener<ContextRefreshedEvent> {
    private final RestTemplate restTemplate;

    public BootStrap(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        configureRestTemplate();

        storeBTCData();
        storeETHData();
        storeXRPData();
    }

    private void storeBTCData() {
        log.info("======================================= Start loading BTC/USD");
        final ResponseEntity<Trade[]> response = restTemplate
                .getForEntity("https://cex.io/api/trade_history/BTC/USD", Trade[].class);
        List<Trade> tradeList = Arrays.asList(response.getBody());
        tradeList.forEach(trade -> trade.setCurrency("BTC"));
        tradeList.forEach(System.out::println);
        log.info("======================================= Finish loading BTC/USD");
    }

    private void storeETHData() {
        log.info("======================================= Start loading ETH/USD");
        final ResponseEntity<Trade[]> response = restTemplate
                .getForEntity("https://cex.io/api/trade_history/ETH/USD", Trade[].class);
        List<Trade> tradeList = Arrays.asList(response.getBody());
        tradeList.forEach(trade -> trade.setCurrency("ETH"));
        tradeList.forEach(System.out::println);
        log.info("======================================= Finish loading ETH/USD");
    }

    private void storeXRPData() {
        log.info("======================================= Start loading XRP/USD");
        final ResponseEntity<Trade[]> response = restTemplate
                .getForEntity("https://cex.io/api/trade_history/XRP/USD", Trade[].class);
        List<Trade> tradeList = Arrays.asList(response.getBody());
        tradeList.forEach(trade -> trade.setCurrency("XRP"));
        tradeList.forEach(System.out::println);
        log.info("======================================= Finish loading XRP/USD");
    }

    private void configureRestTemplate() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }
}
