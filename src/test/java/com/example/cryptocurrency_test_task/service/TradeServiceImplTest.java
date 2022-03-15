package com.example.cryptocurrency_test_task.service;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {

    private TradeService tradeService;
    @Mock
    private TradeRepository tradeRepository;
    private List<Trade> trades;
    private Trade tradeWithMinPrice;
    private Trade tradeWithMaxPrice;


    @BeforeEach
    void setUp() {
        tradeService = new TradeServiceImpl(tradeRepository);
        trades = new ArrayList<>();
        trades.add(Trade.builder().type("sell").amount("0.5").
                price("2549.5").cryptoCurrency("ETH").currency("USD").build());
        tradeWithMinPrice = Trade.builder().type("sell").amount("0.6").
                price("2049.5").cryptoCurrency("ETH").currency("USD").build();
        trades.add(tradeWithMinPrice);
        tradeWithMaxPrice = Trade.builder().type("sell").amount("0.8").
                price("2949.5").cryptoCurrency("ETH").currency("USD").build();
        trades.add(tradeWithMaxPrice);
    }

    @Test
    void testFindTradeWithMinPriceByCryptoCurrency() {

    }

    @Test
    void findTradeWithMaxPriceByCryptoCurrency() {
    }
}