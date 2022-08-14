package com.dk0124.cdr.app.socket.handler.bithumb;


import com.dk0124.cdr.app.service.bithumb.orderbook.BithumbOrderbookService;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.dto.bithumb.orderbook.BithumbOrderbookDto;
import com.dk0124.cdr.entity.bithumb.orderbook.BithumbOrderbookUnit;
import com.dk0124.cdr.entity.bithumb.orderbook.message.BithumbOrderbookListMessage;
import com.dk0124.cdr.entity.bithumb.orderbook.message.BithumbOrderbookSocketMessage;
import com.dk0124.cdr.entity.bithumb.orderbook.message.BithumbOrderbookUnitMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BithumbOrderbookSocketClientHandler extends WebSocketClientPublisherHandler {

    private final BithumbOrderbookService bithumbOrderbookService;
    private final ObjectMapper objectMapper;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String s = String.valueOf(message.getPayload());
        BithumbOrderbookSocketMessage bithumbOrderbookSocketMessage = objectMapper.readValue(s, BithumbOrderbookSocketMessage.class);
        if(bithumbOrderbookSocketMessage.getResmsg()!= null || bithumbOrderbookSocketMessage.getStatus() != null){
            if(bithumbOrderbookSocketMessage.getStatus().equals("0000"))
                log.info("bithumb client socket connected message : {}", s);
            else
                log.error("error on connection | reply : {}", s);
            return;
        }
        BithumbOrderbookDto bithumbOrderbookDto = parseBithumbOrderbookMessage(bithumbOrderbookSocketMessage);
        bithumbOrderbookService.save(bithumbOrderbookDto);
    }

    public BithumbOrderbookDto parseBithumbOrderbookMessage( BithumbOrderbookSocketMessage bithumbOrderbookSocketMessage )  {
        BithumbOrderbookListMessage content = bithumbOrderbookSocketMessage.getContent();
        List<BithumbOrderbookUnitMessage> list = content.getList();
        List<BithumbOrderbookUnit> orderbookUnits = new ArrayList<>();
        for(BithumbOrderbookUnitMessage unit : list){
            orderbookUnits.add(new BithumbOrderbookUnit(unit.getOrderType(),unit.getPrice(),unit.getQuantity(),unit.getTotal()));
        }
        Long datetime = content.getDatetime();
        return BithumbOrderbookDto.builder().orderbookUnits(orderbookUnits).datetime(datetime).code(list.get(0).getCode()).build();

    }
}
