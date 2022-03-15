package com.example.cryptocurrency_test_task.repository;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class TradeRepositoryIntegrationTest {
    @Autowired
    private TradeRepository tradeRepository;

    private List<Trade> trades;
    private Trade tradeWithMinPrice;
    private Trade tradeWithMaxPrice;

    @BeforeEach
    void setUp(){
        trades = new ArrayList<>();
        trades.add(Trade.builder().type("sell").amount("0.5").
                price("2549.5").cryptoCurrency("ETH").currency("USD").build());
        tradeWithMinPrice = Trade.builder().type("sell").amount("0.6").
                price("2049.5").cryptoCurrency("ETH").currency("USD").build();
        trades.add(tradeWithMinPrice);
        tradeWithMaxPrice = Trade.builder().type("sell").amount("0.8").
                price("2949.5").cryptoCurrency("ETH").currency("USD").build();
        trades.add(tradeWithMaxPrice);
        tradeRepository.saveAll(trades);
    }

    @Test
    void testDataShouldNotBeEmpty() {
        assertThat(tradeRepository.findAll()).isNotNull();
        assertThat(tradeRepository.count()).isEqualTo(3L);
    }

    @Test
    void findTopByCryptoCurrencyOrderByPrice() {
        assertThat(tradeRepository.findTopByCryptoCurrencyOrderByPrice("ETH").get().getId())
                .isEqualTo(tradeWithMinPrice.getId());
        assertThat(tradeRepository.findTopByCryptoCurrencyOrderByPrice("ETH").get().getPrice())
                .isEqualTo(tradeWithMinPrice.getPrice());
    }

    @Test
    void findTopByCryptoCurrencyOrderByPriceDesc() {
        assertThat(tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc("ETH").get().getPrice())
                .isEqualTo(tradeWithMaxPrice.getPrice());
        assertThat(tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc("ETH").get().getId())
                .isEqualTo(tradeWithMaxPrice.getId());
    }
}