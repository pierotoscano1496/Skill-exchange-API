package com.main.skillexchangeapi.domain.constants;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    YAPE("yape"), CASH("cash"), CREDIT_CARD("tarjeta");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}
