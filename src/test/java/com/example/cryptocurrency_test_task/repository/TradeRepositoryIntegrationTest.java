package com.example.cryptocurrency_test_task.repository;

import com.example.cryptocurrency_test_task.domain.Trade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class TradeRepositoryIntegrationTest {
    public static final String MIN_PRICE = "2049.5";
    public static final String AVG_PRICE = "2549.5";
    public static final String MAX_PRICE = "2949.5";
    public static final String ETH = "ETH";
    public static final String USD = "USD";
    public static final String TRADE_TYPE = "sell";
    @Autowired
    private TradeRepository tradeRepository;

    private List<Trade> trades;
    public Trade tradeWithMinPrice;
    private Trade tradeWithMaxPrice;

    @BeforeEach
    void setUp(){
        trades = new ArrayList<>();
        trades.add(Trade.builder().type(TRADE_TYPE).amount("0.5").
                price(AVG_PRICE).cryptoCurrency(ETH).currency(USD).build());
        tradeWithMinPrice = Trade.builder().type(TRADE_TYPE).amount("0.6").
                price(MIN_PRICE).cryptoCurrency(ETH).currency(USD).build();
        trades.add(tradeWithMinPrice);
        tradeWithMaxPrice = Trade.builder().type(TRADE_TYPE).amount("0.8").
                price(MAX_PRICE).cryptoCurrency(ETH).currency(USD).build();
        trades.add(tradeWithMaxPrice);
        tradeRepository.saveAll(trades);
    }

    @AfterEach
    void tearDown() {
        tradeRepository.deleteAll();
    }

    @Test
    void testDataShouldNotBeEmpty() {
        assertThat(tradeRepository.findAll()).isNotNull();
        assertThat(tradeRepository.count()).isEqualTo(3L);
    }

    @Test
    void findTopByCryptoCurrencyOrderByPrice() {
        final Trade trade = tradeRepository.findTopByCryptoCurrencyOrderByPrice(ETH).get();
        assertThat(trade.getId())
                .isEqualTo(tradeWithMinPrice.getId());
        assertThat(trade.getPrice())
                .isEqualTo(tradeWithMinPrice.getPrice());
    }

    @Test
    void findTopByCryptoCurrencyOrderByPriceDesc() {
        final Trade trade = tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc(ETH).get();
        assertThat(trade.getPrice())
                .isEqualTo(tradeWithMaxPrice.getPrice());
        assertThat(trade.getId())
                .isEqualTo(tradeWithMaxPrice.getId());
    }

    @Test
    void findByCryptoCurrencyIgnoreCaseOrderByPrice() {
        final List<Trade> tradesList = tradeRepository
                .findByCryptoCurrencyIgnoreCaseOrderByPrice(ETH, PageRequest.of(0, 2)).get();
        assertThat(tradesList.get(0).getPrice()).isEqualTo(MIN_PRICE);
        assertThat(tradesList.get(1).getPrice()).isEqualTo(AVG_PRICE);
        assertThat(tradesList.size()).isEqualTo(2);
    }
}