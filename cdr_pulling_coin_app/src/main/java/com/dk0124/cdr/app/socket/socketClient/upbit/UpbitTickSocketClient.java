package com.dk0124.cdr.app.socket.socketClient.upbit;

import com.dk0124.cdr.app.service.rest.upbit.UpbitRestRequestService;
import com.dk0124.cdr.app.service.upbit.tick.UpbitTickService;
import com.dk0124.cdr.app.socket.clientIndicator.SocketClientIndicator;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.app.socket.socketClient.AbstractSocketClient;
import com.dk0124.cdr.constants.coinCode.CoinCode;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.task.TaskType;
import com.dk0124.cdr.constants.vendor.VendorType;
import com.dk0124.cdr.dto.upbit.tick.UpbitTickDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

@Setter(AccessLevel.PRIVATE)
@Slf4j
public class UpbitTickSocketClient extends AbstractSocketClient {
    private UpbitTickService tickService;

    public UpbitTickSocketClient(UpbitRestRequestService upbitRestRequestService, URI uri, WebSocketClientPublisherHandler socketHandler, TaskType taskType, VendorType vendorType, List<CoinCode> codes) {
        super(upbitRestRequestService, uri, socketHandler, taskType, vendorType, codes);
    }

    public UpbitTickSocketClient(UpbitRestRequestService upbitRestRequestService, URI uri, WebSocketClientPublisherHandler socketHandler, TaskType taskType, VendorType vendorType, List<CoinCode> codes, UpbitTickService tickService) {
        super(upbitRestRequestService, uri, socketHandler, taskType, vendorType, codes);
        this.setTickService(tickService);
    }


    public int getRequiredWaitTime() {
        return 1000;
    }

    @Override
    public void notified(String key) {
        log.info("{} notified : subscriber notified wait for 1000 ", key);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        log.info("{} notified : wakeup and try to reconnect ", key);

        try {
            // TODO :  스트림으로
            for (SocketClientIndicator indicator : sessionMap.keySet()) {
                if (indicator.getSessionId() == key) {
                    CoinCode[] coins = indicator.getCoins();
                    log.error("try to close {} ", indicator.coinsToString());
                    sessionMap.get(indicator).close();
                    sessionMap.remove(indicator);
                    log.error("try to rerun {}", indicator.coinsToString());
                    runSocketClientListener();
                    if (taskType == TaskType.UPBIT_ORDERBOOK)
                        stuffGapBetweenRestart();
                    return;
                }
            }
            throw new Exception("no valid coin ");
        } catch (Exception e) {
            log.info("error during restart socket {}  | message", e.getMessage());
        }
    }

    private void stuffGapBetweenRestart(UpbitCoinCode code) throws JsonProcessingException, InterruptedException {
        Thread.sleep(100);
        List<UpbitTickDto> ticks = upbitRestRequestService.getLastestTicks(code);
        log.info("call rest api / codes : {}", codes);
        for (UpbitTickDto tickDto : ticks) {
            tickService.saveWhenNotExist(tickDto);
        }
    }

    public void stuffGapBetweenRestart() throws JsonProcessingException, InterruptedException {
        for (CoinCode code : codes) {
            stuffGapBetweenRestart((UpbitCoinCode) code);
        }
    }

}
