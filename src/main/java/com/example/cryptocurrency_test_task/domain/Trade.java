package com.example.cryptocurrency_test_task.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document
public class Trade {
    @Id
    private String id;
    private String tid;
    private String type;
    private String amount;
    private String price;
    private String date;
    private String cryptoCurrency;
    private String currency;

    //Builder for testing purposes
    @Builder
    public Trade(String type, String amount, String price,
                 String cryptoCurrency, String currency) {
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.cryptoCurrency = cryptoCurrency;
        this.currency = currency;
    }
}
