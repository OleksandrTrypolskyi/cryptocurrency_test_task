package com.example.cryptocurrency_test_task.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
}
