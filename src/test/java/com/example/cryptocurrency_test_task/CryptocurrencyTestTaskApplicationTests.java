package com.example.cryptocurrency_test_task;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CryptocurrencyTestTaskApplicationTests {


	@Autowired
	private TradeRepository tradeRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void bootStrapDataTest() {
		final List<Trade> tradeRepositoryAll = tradeRepository.findAll();
		assertThat((long) tradeRepositoryAll.size()).isGreaterThan(1L);
		assertThat(tradeRepositoryAll).isNotNull();
	}
}
