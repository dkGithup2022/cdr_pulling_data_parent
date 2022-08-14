package com.dk0124.cdr.app.task;


import com.dk0124.cdr.app.socket.clientIndicator.SocketClientIndicator;
import com.dk0124.cdr.app.socket.socketClient.AbstractSocketClient;
import com.dk0124.cdr.constants.task.TaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReconnectSocketClient {
    private final Map<TaskType, AbstractSocketClient> taskMap;

    @Scheduled(cron = "${property.upbitCron.tick.restartSessions.cronCommand}")
    public void reconnectUpbitTickClient() throws IOException, Exception {
        reconnectSocketClient(TaskType.UPBIT_TICK);
    }

    @Scheduled(cron = "${property.upbitCron.orderbook.restartSessions.cronCommand}")
    public void reconnectUpbitOrderBookClient() throws IOException, Exception {
        reconnectSocketClient(TaskType.UPBIT_ORDERBOOK);
    }

    @Scheduled(cron = "${property.bithumbCron.tick.restartSessions.cronCommand}")
    public void reconnectBithumbTickClient() throws IOException, Exception {
        reconnectSocketClient(TaskType.BITHUMB_TICK);
    }

    @Scheduled(cron = "${property.bithumbCron.orderbook.restartSessions.cronCommand}")
    public void reconnectBithumbOrderbookClient() throws IOException, Exception {
        reconnectSocketClient(TaskType.BITHUMB_ORDERBOOK);
    }

    public void reconnectSocketClient(TaskType taskType) throws IOException, Exception {
        AbstractSocketClient socketClient = taskMap.get(taskType);
        Map<SocketClientIndicator, WebSocketSession> sessionMap = socketClient.getSessionMap();
        for (SocketClientIndicator key : sessionMap.keySet()) {
            sessionMap.get(key).close();
        }
        sessionMap.clear();
        log.info("try to close {} ", taskType.getName());
        socketClient.runSocketClientListenerSafe();
        Thread.sleep(socketClient.getRequiredWaitTime());
        listAllSessionsInTask();
        log.info(" reconnectSocketClient | {} sessions restarted ", taskType.getName());
    }

    private void listAllSessionsInTask() {
        for (TaskType taskType : taskMap.keySet()) {
            AbstractSocketClient socketClient = taskMap.get(taskType);
            log.info("taskType: {}  | sessionMap : {}", taskType.getName(), socketClient.getSessionMap().toString());
        }
    }
}
