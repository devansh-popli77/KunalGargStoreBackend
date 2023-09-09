package com.lcwd.store.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails {
    private String orderId;
    private String currency;
    private Integer amount;
    private String key;
}
