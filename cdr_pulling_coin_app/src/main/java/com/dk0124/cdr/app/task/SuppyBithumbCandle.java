package com.dk0124.cdr.app.task;


import com.dk0124.cdr.app.service.rest.upbit.UpbitRestRequestService;
import com.dk0124.cdr.app.service.upbit.candle.UpbitCandleService;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.dto.upbit.candle.UpbitCandleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SuppyBithumbCandle {

    private final UpbitRestRequestService upbitRestRequestService;
    private final UpbitCandleService upbitCandleService;

    @Scheduled(cron = "${property.upbitCron.candle.period.cronCommand}")
    public void upbitCandleSupply() throws JsonProcessingException, InterruptedException {
        Long current = System.currentTimeMillis();
        log.info("upbitCandleSupply | current time : {}", current);
        for (UpbitCoinCode code : UpbitCoinCode.values()) {
            upbitRestRequestService.pauseAfterRestRequest();
            List<UpbitCandleDto> candles = upbitRestRequestService.getMinutesCandles(code, current, 10);
            candles.stream().forEach(c -> upbitCandleService.safeSave(c));
        }
    }
}
