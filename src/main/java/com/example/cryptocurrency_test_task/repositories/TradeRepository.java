package com.example.cryptocurrency_test_task.repositories;

import com.example.cryptocurrency_test_task.domain.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TradeRepository extends MongoRepository<Trade, Long> {
}
