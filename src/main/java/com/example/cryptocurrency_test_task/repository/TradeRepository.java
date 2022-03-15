package com.example.cryptocurrency_test_task.repository;

import com.example.cryptocurrency_test_task.domain.Trade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TradeRepository extends MongoRepository<Trade, String> {
    Optional<Trade> findTopByCryptoCurrencyOrderByPrice(String cryptoCurrency);
    Optional<Trade> findTopByCryptoCurrencyOrderByPriceDesc(String cryptoCurrency);
    Optional<List<Trade>> findByCryptoCurrencyIgnoreCaseOrderByPrice(String cryptoCurrency, Pageable pageable);

}
