package com.dk0124.cdr.app.service.rest.upbit;


import com.dk0124.cdr.constants.Uri;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.dto.upbit.candle.UpbitCandleDto;
import com.dk0124.cdr.dto.upbit.orderbook.UpbitOrderbookDto;
import com.dk0124.cdr.dto.upbit.tick.UpbitTickDto;
import com.dk0124.cdr.entity.upbit.CallType;
import com.dk0124.cdr.entity.upbit.candle.UpbitCandle;
import com.dk0124.cdr.entity.upbit.orderbook.UpbitOrderbook;
import com.dk0124.cdr.entity.upbit.tick.UpbitTick;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpbitRestRequestService {

    private final ObjectMapper objectMapper;

    public List<UpbitOrderbookDto> getLatestOrderBooks(UpbitCoinCode upbitCoinCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String uri = Uri.UPBIT_REST_ORDERBOOK_URI.getAddress();
        uri += "?market=" + upbitCoinCode.toString();
        log.info(" getLatestOrderBooks Uri : {}", uri);
        ResponseEntity<String> response
                = restTemplate.getForEntity(uri, String.class);
        List<UpbitOrderbookDto> books = Arrays.asList(objectMapper.readValue(response.getBody(), UpbitOrderbookDto[].class));
        return books;
    }


    //TODO : string builder
    public List<UpbitTickDto> getLastestTicks(UpbitCoinCode upbitCoinCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String uri = Uri.UPBIT_REST_TICK_URI.getAddress();
        uri += "?market=" + upbitCoinCode.toString();
        uri += "&count=100";
        log.info ( "getLastestTicks() uri : {}", uri );
        ResponseEntity<String> response
                = restTemplate.getForEntity(uri, String.class);
        List<UpbitTickDto> ticks = Arrays.asList(objectMapper.readValue(response.getBody(), UpbitTickDto[].class));
        ticks.stream().forEach(t -> t.setCallType(CallType.RESTAPI));
        return ticks;
    }

    public List<UpbitTickDto> getTicksBeforeSequentialId(UpbitCoinCode upbitCoinCode, Long sequentialId) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String uri = Uri.UPBIT_REST_TICK_URI.getAddress();
        uri += "?market=" + upbitCoinCode.toString();
        uri += "&count=200";
        uri += "&cursor=" + sequentialId;
        log.info("Call rest api :  {}", uri);
        ResponseEntity<String> response
                = restTemplate.getForEntity(uri, String.class);
        // 에러 처리 ... TOO many req

        List<UpbitTickDto> list =Arrays.asList(objectMapper.readValue(response.getBody(),UpbitTickDto[].class));
        list.stream().forEach(t->{
            t.setTradeTimestamp(t.getSequentialId()/1000);
            t.setCallType(CallType.RESTAPI);
            t.setType("trade");
        });
        return list;

    }


    public void pauseAfterRestRequest() throws InterruptedException {
        Thread.sleep(400);
    }

    public List<UpbitTickDto> getTicksBetweenTimeStampFromRest(UpbitCoinCode code, Long from, Long to) throws InterruptedException {
        List<UpbitTickDto> currentTicks = getTicksBeforeSequentialIdSafe(code, to * 1000);
        currentTicks = currentTicks.stream()
                .distinct()
                .sorted(Comparator.comparing(UpbitTickDto::getSequentialId))
                .collect(Collectors.toList());

        while (currentTicks.get(0).getSequentialId() > from * 1000) {
            List<UpbitTickDto> restTicks = getTicksBeforeSequentialIdSafe(code, currentTicks.get(0).getSequentialId());
            // TODO : error 처리 -> try catch 로 변경.
            if (restTicks.size() == 0) {
                log.error("on sequential id: {} & code : {} , return size = 0", currentTicks.get(0).getSequentialId(), code);
                break;
            }

            restTicks = restTicks.stream().sorted(Comparator.comparing(UpbitTickDto::getSequentialId)).collect(Collectors.toList());
            currentTicks = Stream.concat(restTicks.stream(), currentTicks.stream()).collect(Collectors.toList());
            pauseAfterRestRequest();
        }

        currentTicks = currentTicks.stream().filter(t -> t.getSequentialId() >= from * 1000).collect(Collectors.toList());
        return currentTicks;
    }


    private List<UpbitTickDto> getTicksBeforeSequentialIdSafe(UpbitCoinCode code, Long toSequentialId) throws HttpClientErrorException, InterruptedException {
        List<UpbitTickDto> restTicks = new ArrayList<>();
        try {
            restTicks =getTicksBeforeSequentialId(code, toSequentialId);
            pauseAfterRestRequest();
        } catch (HttpClientErrorException clientErrorException) {
            if (clientErrorException.getRawStatusCode() == 429) {
                Thread.sleep(2000);
                return getTicksBeforeSequentialIdSafe(code, toSequentialId);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return restTicks;
    }

    public List<UpbitCandleDto> getMinutesCandles(UpbitCoinCode code, Long millis, int count ) throws JsonProcessingException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        String baseUrl = Uri.UPBIT_REST_CANDLE_MINUTES_URI.getAddress();
        String marketParam = "?market="+ code.toString();
        String to = "&to="+ sdf.format(new Date(millis));
        String suffix = "&count=10";
        String url = baseUrl+marketParam+to+suffix;
        log.info("getLastMinutesCandles () :  url : {}" , url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            List<UpbitCandleDto> candles = Arrays.asList(objectMapper.readValue(response.getBody(),UpbitCandleDto[].class));
            return candles;
        }catch(HttpClientErrorException clientErrorException){
            if (clientErrorException.getRawStatusCode() == 429) {
                pauseAfterRestRequest();
                return getMinutesCandles(code,millis,count);
            }
            log.error("getMinutesCandles | code: {} , millis : {}, count ; {}", code, millis, count);
            // todo : throw new custom error   & 아래 리턴 지우기.
        }

        return new ArrayList<UpbitCandleDto>();

    }

}
