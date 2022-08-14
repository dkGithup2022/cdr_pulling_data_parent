package com.dk0124.cdr.constants.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskType {
    UPBIT_TICK("UPBIT_TICK"),
    UPBIT_ORDERBOOK("UPBIT_ORDERBOOK"),
    BITHUMB_TICK("BITHUMB_TICK"),
    BITHUMB_ORDERBOOK("BITHUMB_ORDERBOOK");

    private String name;
}
