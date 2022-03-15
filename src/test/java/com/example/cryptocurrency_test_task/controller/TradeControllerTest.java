package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TradeControllerTest {

    public static final String MIN_PRICE = "2049.5";
    public static final String MAX_PRICE = "2949.5";
    @Mock
    private TradeService tradeService;
    private TradeController tradeController;
    private MockMvc mockMvc;

    public Trade tradeWithMinPrice;
    public Trade tradeWithMaxPrice;
    private List<Trade> trades;

    @BeforeEach
    void setUp() {
        tradeController = new TradeController(tradeService);
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
        tradeWithMinPrice = Trade.builder().type("sell").amount("0.6").
                price(MIN_PRICE).cryptoCurrency("ETH").currency("USD").build();
        tradeWithMaxPrice = Trade.builder().type("sell").amount("0.8").
                price(MAX_PRICE).cryptoCurrency("ETH").currency("USD").build();
        trades = new ArrayList<>();
        trades.add(tradeWithMinPrice);
        trades.add(tradeWithMaxPrice);
    }

    @Test
    void getTradeWithMinPriceByCryptoCurrency() throws Exception {
        when(tradeService.findTradeWithMinPriceByCryptoCurrency(anyString())).thenReturn(tradeWithMinPrice);
        mockMvc.perform(get("/cryptocurrencies/api/v1/minprice?name=ETH")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cryptoCurrency", is("ETH")))
                .andExpect(jsonPath("$.price", is(MIN_PRICE)));
        verify(tradeService, times(1)).findTradeWithMinPriceByCryptoCurrency(anyString());
    }

    @Test
    void getTradeWithMaxPriceByCryptoCurrency() throws Exception {
        when(tradeService.findTradeWithMaxPriceByCryptoCurrency(anyString())).thenReturn(tradeWithMaxPrice);
        mockMvc.perform(get("/cryptocurrencies/api/v1/maxprice?name=ETH")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cryptoCurrency", is("ETH")))
                .andExpect(jsonPath("$.price", is(MAX_PRICE)));
        verify(tradeService, times(1)).findTradeWithMaxPriceByCryptoCurrency(anyString());
    }

    @Test
    void findByCryptoCurrencyIgnoreCaseOrderByPrice() throws Exception {
        when(tradeService.findByCryptoCurrencyIgnoreCaseOrderByPrice(anyString(), any()))
                .thenReturn(trades);
        mockMvc.perform(get("/cryptocurrencies/api/v1/?name=ETH")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price", is(MIN_PRICE)))
                .andExpect(jsonPath("$[1].price", is(MAX_PRICE)));
    }
}