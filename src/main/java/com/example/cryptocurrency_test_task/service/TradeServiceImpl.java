package com.example.cryptocurrency_test_task.service;

import com.example.cryptocurrency_test_task.domain.Trade;
import com.example.cryptocurrency_test_task.exceptions.NotSupportedCurrencyException;
import com.example.cryptocurrency_test_task.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final Set<String> cryptoCurrencies = Set.of("BTC", "ETH", "XRP");

    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Trade findTradeWithMinPriceByCryptoCurrency(String cryptoCurrency) {
        return tradeRepository
                .findTopByCryptoCurrencyOrderByPrice(cryptoCurrency)
                .orElseThrow(getNotSupportedCurrencyExceptionSupplier(cryptoCurrency));
    }

    @Override
    public Trade findTradeWithMaxPriceByCryptoCurrency(String cryptoCurrency) {
        return tradeRepository
                .findTopByCryptoCurrencyOrderByPriceDesc(cryptoCurrency)
                .orElseThrow(getNotSupportedCurrencyExceptionSupplier(cryptoCurrency));
    }

    @Override
    public List<Trade> findByCryptoCurrencyIgnoreCaseOrderByPrice(String cryptoCurrency, Pageable pageable) {
        return tradeRepository
                .findByCryptoCurrencyIgnoreCaseOrderByPrice(cryptoCurrency, pageable)
                .orElseThrow(getNotSupportedCurrencyExceptionSupplier(cryptoCurrency));

    }

    @Override
    public void generateCSVReport(HttpServletResponse response) {
        String filename = "report.csv";
        final List<String> cryptos = List.of("BTC", "ETH", "XRP");
        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("Name", "Min Price", "Max Price"))) {
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"");
            for (String cryptoCurrency : cryptos) {
                String name = cryptoCurrency;
                String minPrice = findTradeWithMinPriceByCryptoCurrency(cryptoCurrency).getPrice();
                String maxPrice = findTradeWithMaxPriceByCryptoCurrency(cryptoCurrency).getPrice();
                csvPrinter.printRecord(name, minPrice, maxPrice);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Supplier<NotSupportedCurrencyException> getNotSupportedCurrencyExceptionSupplier(String cryptoCurrency) {
        return () -> new NotSupportedCurrencyException("Crypto currency " + cryptoCurrency +
                " is not supported. " + "Please use: 'BTC', 'ETH', 'XRP'.");
    }
}
