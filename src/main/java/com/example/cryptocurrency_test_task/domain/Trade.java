package com.example.cryptocurrency_test_task.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Trade {
    private String tid;
    private String type;
    private String amount;
    private String price;
    private String date;
    private String cryptoCurrency;
    private String currency;
}
