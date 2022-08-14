package com.dk0124.cdr.app.socket.handler.bithumb;


import com.dk0124.cdr.app.service.bithumb.tick.BithumbTickService;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.dto.bithumb.tick.BithumbTickDto;
import com.dk0124.cdr.entity.bithumb.tick.message.BithumbSocketTickMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class BithumbTickSocketClientHandler extends WebSocketClientPublisherHandler {

    private final BithumbTickService bithumbTickService;
    private final ObjectMapper objectMapper;


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String s = String.valueOf(message.getPayload());
        BithumbSocketTickMessage bithumbSocketTickMessage = objectMapper.readValue(s, BithumbSocketTickMessage.class);

        if (bithumbSocketTickMessage.getStatus() != null && bithumbSocketTickMessage.getStatus().equals("0000"))
            return;

        for (BithumbTickDto tickDto : bithumbSocketTickMessage.getContent().getList()) {
            tickDto.setTimestamp(tickDto.getContDtm().getTime());
            bithumbTickService.save(tickDto);
        }

    }
}
