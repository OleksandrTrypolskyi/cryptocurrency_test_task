package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.exceptions.NotSupportedCurrencyException;
import com.example.cryptocurrency_test_task.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TradeControllerTest {

    public static final String MIN_PRICE = "2049.5";
    public static final String MAX_PRICE = "2949.5";
    public static final String EXCEPTION_MESSAGE = "Crypto currency ET is not supported." +
            " Please use: 'BTC', 'ETH', 'XRP'.";
    public static final String PLEASE_USE_BTC_ETH_XRP = "Please use: 'BTC', 'ETH', 'XRP'.";
    public static final String CRYPTOCURRENCIES_API_V_1 = "/cryptocurrencies/api/v1/";
    public static final String ETH = "ETH";
    public static final String USD = "USD";
    public static final String SELL = "sell";
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
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController)
                .setControllerAdvice(new ControllerExceptionHandler()).build();
        tradeWithMinPrice = Trade.builder().type(SELL).amount("0.6").
                price(MIN_PRICE).cryptoCurrency(ETH).currency(USD).build();
        tradeWithMaxPrice = Trade.builder().type(SELL).amount("0.8").
                price(MAX_PRICE).cryptoCurrency(ETH).currency(USD).build();
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
                .andExpect(jsonPath("$.cryptoCurrency", is(ETH)))
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
                .andExpect(jsonPath("$.cryptoCurrency", is(ETH)))
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

    @Test
    void getTradeWithMinPriceByCryptoCurrencyThrowsExc() throws Exception {
        when(tradeService.findTradeWithMinPriceByCryptoCurrency(anyString()))
                .thenThrow(new NotSupportedCurrencyException(EXCEPTION_MESSAGE));

        mockMvc.perform(get("/cryptocurrencies/api/v1/minprice?name=ET")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "ET"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString(PLEASE_USE_BTC_ETH_XRP)));
    }

    @Test
    void getTradeWithMaxPriceByCryptoCurrencyThrowsExc() throws Exception {
        when(tradeService.findTradeWithMaxPriceByCryptoCurrency(anyString()))
                .thenThrow(new NotSupportedCurrencyException(EXCEPTION_MESSAGE));

        mockMvc.perform(get("/cryptocurrencies/api/v1/maxprice?name=ET")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "ET"))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.message",
                        containsString(PLEASE_USE_BTC_ETH_XRP)));
    }

    @Test
    @DisplayName("findByCryptoCurrencyIgnoreCaseOrderByPrice() throws NotSupportedCurrencyException")
    void findByCryptoCurrencyIgnoreCaseOrderByPriceThrowsExc() throws Exception {
        when(tradeService.findByCryptoCurrencyIgnoreCaseOrderByPrice(anyString(), any()))
                .thenThrow(new NotSupportedCurrencyException(EXCEPTION_MESSAGE));

        mockMvc.perform(get(CRYPTOCURRENCIES_API_V_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "ET"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString(PLEASE_USE_BTC_ETH_XRP)));
    }

    @Test
    @DisplayName("findByCryptoCurrencyIgnoreCaseOrderByPrice() throws NumberFormatException")
    void findByCryptoCurrencyIgnoreCaseOrderByPriceThrowsNumbrExc() throws Exception {
        mockMvc.perform(get(CRYPTOCURRENCIES_API_V_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "ET")
                        .param("page", "text"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString("nested exception is java.lang.NumberFormatException")));
    }
}