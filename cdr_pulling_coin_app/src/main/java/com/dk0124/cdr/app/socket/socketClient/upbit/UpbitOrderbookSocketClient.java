package com.dk0124.cdr.app.socket.socketClient.upbit;


import com.dk0124.cdr.app.service.rest.upbit.UpbitRestRequestService;
import com.dk0124.cdr.app.service.upbit.orderbook.UpbitOrderbookService;
import com.dk0124.cdr.app.socket.clientIndicator.SocketClientIndicator;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.app.socket.socketClient.AbstractSocketClient;
import com.dk0124.cdr.constants.coinCode.CoinCode;
import com.dk0124.cdr.constants.task.TaskType;
import com.dk0124.cdr.constants.vendor.VendorType;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

@Setter(AccessLevel.PRIVATE)
@Slf4j
public class UpbitOrderbookSocketClient  extends AbstractSocketClient {

    UpbitOrderbookService orderBookService;

    public UpbitOrderbookSocketClient(UpbitRestRequestService upbitRestRequestService, URI uri, WebSocketClientPublisherHandler socketHandler, TaskType taskType, VendorType vendorType, List<CoinCode> codes) {
        super(upbitRestRequestService, uri,  socketHandler, taskType, vendorType, codes);
    }

    public UpbitOrderbookSocketClient(UpbitRestRequestService upbitRestRequestService, URI uri,   WebSocketClientPublisherHandler socketHandler, TaskType taskType, VendorType vendorType, List<CoinCode> codes, UpbitOrderbookService orderBookService) {
        super(upbitRestRequestService, uri,  socketHandler, taskType, vendorType, codes);
        this.setOrderBookService(orderBookService);
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
            for (SocketClientIndicator indicator : sessionMap.keySet()) {
                if (indicator.getSessionId() == key) {
                    log.error("try to close {} ", indicator.coinsToString());
                    sessionMap.get(indicator).close();
                    sessionMap.remove(indicator);
                    log.error("try to rerun {}", indicator.coinsToString());
                    runSocketClientListener();
                }
            }
        } catch (Exception e) {
            log.info("error during restart socket {}  | message", e.getMessage());
        }
    }
}
