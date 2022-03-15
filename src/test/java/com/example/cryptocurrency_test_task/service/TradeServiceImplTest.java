package com.example.cryptocurrency_test_task.service;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.exceptions.NotSupportedCurrencyException;
import com.example.cryptocurrency_test_task.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {

    public static final String MIN_PRICE = "2049.5";
    public static final String MAX_PRICE = "2949.5";
    public static final String AVG_PRICE = "2549.5";
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
        tradeWithMinPrice = Trade.builder().type("sell").amount("0.6").
                price(MIN_PRICE).cryptoCurrency("ETH").currency("USD").build();
        trades.add(tradeWithMinPrice);
        trades.add(Trade.builder().type("sell").amount("0.5").
                price(AVG_PRICE).cryptoCurrency("ETH").currency("USD").build());
        tradeWithMaxPrice = Trade.builder().type("sell").amount("0.8").
                price(MAX_PRICE).cryptoCurrency("ETH").currency("USD").build();
        trades.add(tradeWithMaxPrice);
    }

    @Test
    void testFindTradeWithMinPriceByCryptoCurrency() {
        when(tradeRepository.findTopByCryptoCurrencyOrderByPrice("ETH"))
                .thenReturn(Optional.of(tradeWithMinPrice));
        assertThat(tradeService.findTradeWithMinPriceByCryptoCurrency("ETH").getPrice())
                .isEqualTo(MIN_PRICE);
        verify(tradeRepository, times(1)).findTopByCryptoCurrencyOrderByPrice("ETH");
    }

    @Test
    void findTradeWithMaxPriceByCryptoCurrency() {
        when(tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc("ETH"))
                .thenReturn(Optional.of(tradeWithMaxPrice));
        assertThat(tradeService.findTradeWithMaxPriceByCryptoCurrency("ETH").getPrice())
                .isEqualTo(MAX_PRICE);
        verify(tradeRepository, times(1)).findTopByCryptoCurrencyOrderByPriceDesc("ETH");
    }

    @Test
    void findByCryptoCurrencyIgnoreCaseOrderByPrice() {
        when(tradeRepository.findByCryptoCurrencyIgnoreCaseOrderByPrice(anyString(), any()))
                .thenReturn(Optional.of(List.of(trades.get(0), trades.get(1))));
        final List<Trade> trades = tradeService
                .findByCryptoCurrencyIgnoreCaseOrderByPrice("ETH", PageRequest.of(0, 2));

        assertThat(trades.get(0).getPrice()).isEqualTo(MIN_PRICE);
        assertThat(trades.get(1).getPrice()).isEqualTo(AVG_PRICE);
        assertThat(trades.size()).isEqualTo(2);
        assertThat(trades.contains(tradeWithMaxPrice)).isFalse();
    }

    @DisplayName("testFindTradeWithMinPriceByCryptoCurrency() throws NotSupportedCurrencyException")
    @Test
    void testFindTradeWithMinPriceByCryptoCurrencyThrowsExc() {
        assertThrows(NotSupportedCurrencyException.class,
                () -> tradeService.findTradeWithMinPriceByCryptoCurrency("BT"));
    }

    @DisplayName("testFindTradeWithMaxPriceByCryptoCurrency() throws NotSupportedCurrencyException")
    @Test
    void testFindTradeWithMaxPriceByCryptoCurrencyThrowsExc() {
        assertThrows(NotSupportedCurrencyException.class,
                () -> tradeService.findTradeWithMaxPriceByCryptoCurrency("BT"));
    }

    @DisplayName("findByCryptoCurrencyIgnoreCaseOrderByPrice() throws NotSupportedCurrencyException")
    @Test
    void findByCryptoCurrencyIgnoreCaseOrderByPriceThrowsExc() {
        assertThrows(NotSupportedCurrencyException.class,
                () -> tradeService.findByCryptoCurrencyIgnoreCaseOrderByPrice("BT",
                        PageRequest.of(0, 2)));
    }
}