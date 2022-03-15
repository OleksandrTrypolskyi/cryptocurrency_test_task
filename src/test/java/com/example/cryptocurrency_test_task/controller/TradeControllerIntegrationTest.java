package com.example.cryptocurrency_test_task.controller;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.repository.TradeRepository;
import com.example.cryptocurrency_test_task.service.TradeService;
import com.example.cryptocurrency_test_task.service.TradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.CharInput;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class TradeControllerIntegrationTest {

    public static final String BTC = "BTC";
    public static final String ETH = "ETH";
    public static final String XRP = "XRP";
    @Mock
    private TradeRepository tradeRepository;
    private TradeController tradeController;
    private TradeService tradeService;
    private MockMvc mockMvc;

    private Trade btcTradeWithMinPrice;
    private Trade btcTradeWithMaxPrice;

    private Trade ethTradeWithMinPrice;
    private Trade ethTradeWithMaxPrice;

    private Trade xrpTradeWithMinPrice;
    private Trade xrpTradeWithMaxPrice;

    @BeforeEach
    void setUp() {
        tradeService = new TradeServiceImpl(tradeRepository);
        tradeController = new TradeController(tradeService);
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();

        btcTradeWithMinPrice = Trade.builder().price("10").cryptoCurrency("BTC").build();
        btcTradeWithMaxPrice = Trade.builder().price("1000").cryptoCurrency("BTC").build();

        ethTradeWithMinPrice = Trade.builder().price("20").cryptoCurrency(ETH).build();
        ethTradeWithMaxPrice = Trade.builder().price("2000").cryptoCurrency(ETH).build();

        xrpTradeWithMinPrice = Trade.builder().price("50").cryptoCurrency(XRP).build();
        xrpTradeWithMaxPrice = Trade.builder().price("5000").cryptoCurrency(XRP).build();
    }

    @Test
    void generateCSVReport() throws Exception {
        String expectedResult =
                "Name,Min Price,Max Price \n" +
                        "BTC,10,1000 \n" +
                        "ETH,20,2000 \n" +
                        "XRP,50,5000 \n";
        when(tradeRepository.findTopByCryptoCurrencyOrderByPrice(BTC))
                .thenReturn(Optional.of(btcTradeWithMinPrice));
        when(tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc(BTC))
                .thenReturn(Optional.of(btcTradeWithMaxPrice));

        when(tradeRepository.findTopByCryptoCurrencyOrderByPrice(ETH))
                .thenReturn(Optional.of(ethTradeWithMinPrice));
        when(tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc(ETH))
                .thenReturn(Optional.of(ethTradeWithMaxPrice));

        when(tradeRepository.findTopByCryptoCurrencyOrderByPrice(XRP))
                .thenReturn(Optional.of(xrpTradeWithMinPrice));
        when(tradeRepository.findTopByCryptoCurrencyOrderByPriceDesc(XRP))
                .thenReturn(Optional.of(xrpTradeWithMaxPrice));


        final MvcResult mvcResult = mockMvc.perform(get("/cryptocurrencies/api/v1/csv"))
                .andExpect(content().contentType("text/csv"))
                .andExpect(header()
                        .string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\""))
                .andExpect(status().isOk()).andReturn();

        final String actualResult = mvcResult.getResponse().getContentAsString().replaceAll("\\s", "");
        assertThat(actualResult).isEqualTo(expectedResult.replaceAll("\\s", ""));
    }
}