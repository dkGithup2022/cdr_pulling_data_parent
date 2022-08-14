package com.dk0124.cdr.app.service.rest.bithumb;


import com.dk0124.cdr.constants.Uri;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.dto.bithumb.candle.BithumbCandleDto;
import com.dk0124.cdr.entity.bithumb.candle.BithumbCandle;
import com.dk0124.cdr.entity.bithumb.candle.message.BithumbRestCandleMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BithumbRestRequestService {
    private final ObjectMapper objectMapper;

    public int getSleepMillis() {return 1000;}

    public List<BithumbCandleDto> getLastestCandles(BithumbCoinCode code) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String uri  = Uri.BITHUMB_REST_CANDLE_URI.getAddress();
        uri+= "/" + code.toString();
        uri+= "/1m";
        ResponseEntity<String> response
                = restTemplate.getForEntity(uri, String.class);
        BithumbRestCandleMessage brcm =  objectMapper.readValue(response.getBody(),BithumbRestCandleMessage.class);
        Object[] candleData = brcm.getData();
        ArrayList<BithumbCandleDto> bithumbCandles = new ArrayList<BithumbCandleDto>();
        for(Object data : candleData){
            ArrayList<Object> numbericData = (ArrayList<Object>) data;
            BithumbCandleDto c = new BithumbCandleDto(
                    code.toString()
                    ,Long.valueOf(String.valueOf(numbericData.get(0)))
                    ,Double.valueOf(String.valueOf(numbericData.get(1)))
                    ,Double.valueOf(String.valueOf(numbericData.get(2)))
                    ,Double.valueOf(String.valueOf(numbericData.get(3)))
                    ,Double.valueOf(String.valueOf(numbericData.get(4)))
                    ,Double.valueOf(String.valueOf(numbericData.get(5)))
            );
            bithumbCandles.add(c);
        }
        return bithumbCandles;
    }
}
