package com.dk0124.cdr.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Uri {
    UPBIT_SOCKET_URI("wss://api.upbit.com/websocket/v1"),
    UPBIT_REST_TICK_URI("https://api.upbit.com/v1/trades/ticks"),
    UPBIT_REST_ORDERBOOK_URI("https://api.upbit.com/v1/orderbook"),
    UPBIT_REST_CANDLE_MINUTES_URI("https://api.upbit.com/v1/candles/minutes/1"),
    BITHUMB_SOCKET_URI("wss://pubwss.bithumb.com/pub/ws"),
    BITHUMB_REST_CANDLE_URI("https://api.bithumb.com/public/candlestick");
    private String address;
}
