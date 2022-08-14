package com.dk0124.cdr.app.socket.socketClient.bithumb;


import com.dk0124.cdr.app.service.bithumb.tick.BithumbTickService;
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

@Slf4j
@Setter(AccessLevel.PRIVATE)
public class BithumbTickSocketClient extends AbstractSocketClient {

    private BithumbTickService bithumbTickService;

    public BithumbTickSocketClient(URI uri, WebSocketClientPublisherHandler socketHandler, TaskType taskType, VendorType vendorType, List<CoinCode> codes) {
        super(uri, socketHandler, taskType, vendorType, codes);
    }

    public BithumbTickSocketClient( URI uri, WebSocketClientPublisherHandler socketHandler, TaskType taskType, VendorType vendorType, List<CoinCode> codes, BithumbTickService tickService) {
        super( uri, socketHandler, taskType, vendorType, codes);
        this.setBithumbTickService(tickService);
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
                }
            }
            throw new Exception("no valid coin ");
        } catch (Exception e) {
            log.info("error during restart socket {}  | message", e.getMessage());
        }
    }

}
