package com.dk0124.cdr.app.socket.handler.upbit;


import com.dk0124.cdr.app.service.upbit.tick.UpbitTickService;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.dto.upbit.tick.UpbitTickDto;
import com.dk0124.cdr.entity.upbit.CallType;
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
public class UpbitTickSocketClientHandler extends WebSocketClientPublisherHandler {

    private final UpbitTickService upbitTickService;
    private final ObjectMapper objectMapper;


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String s = StandardCharsets.UTF_8.decode((ByteBuffer) message.getPayload()).toString();
        UpbitTickDto tick = objectMapper.readValue(s, UpbitTickDto.class);
        tick.setCallType(CallType.SOCKET);
        upbitTickService.save(tick);
    }

}
