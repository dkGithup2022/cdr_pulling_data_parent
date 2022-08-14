package com.dk0124.cdr.app.socket.handler.upbit;

import com.dk0124.cdr.app.service.upbit.orderbook.UpbitOrderbookService;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.dto.upbit.orderbook.UpbitOrderbookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@RequiredArgsConstructor
public class UpbitOrderbookSocketClientHandler extends WebSocketClientPublisherHandler {

    private final UpbitOrderbookService upbitOrderBookService;
    private final ObjectMapper objectMapper;

    // MEMO (1)
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String s = StandardCharsets.UTF_8.decode((ByteBuffer) message.getPayload()).toString();
        UpbitOrderbookDto book = objectMapper.readValue(s, UpbitOrderbookDto.class);
        upbitOrderBookService.save(book);
    }
}
