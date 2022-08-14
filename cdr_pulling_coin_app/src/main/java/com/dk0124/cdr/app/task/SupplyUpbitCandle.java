package com.dk0124.cdr.app.task;


import com.dk0124.cdr.app.service.bithumb.candle.BithumbCandleService;
import com.dk0124.cdr.app.service.rest.bithumb.BithumbRestRequestService;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.dto.bithumb.candle.BithumbCandleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SupplyUpbitCandle {

    private final BithumbRestRequestService bithumbRestRequestService;
    private final BithumbCandleService bithumbCandleService;

    @Scheduled(cron = "${property.upbitCron.candle.period.cronCommand}")
    public void bithumbCandleSupply() throws IOException, InterruptedException {
        log.info(" bithumbCandleSupply()  | timestamp : {} |  start supply bithumb candle data",System.currentTimeMillis() );
        for(BithumbCoinCode code : BithumbCoinCode.values()){
            Thread.sleep(bithumbRestRequestService.getSleepMillis());
            log.info("  bithumbCandleSupply()  | timestamp : {} |  supply candle : {}", System.currentTimeMillis(),code.toString() );
            List<BithumbCandleDto> candles = bithumbRestRequestService.getLastestCandles(code);
            candles.stream().forEach(c -> bithumbCandleService.safeSave(c));
        }
    }
}
