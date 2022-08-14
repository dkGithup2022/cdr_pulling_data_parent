package com.dk0124.cdr.app.socket.socketClient;

import com.dk0124.cdr.app.service.bithumb.orderbook.BithumbOrderbookService;
import com.dk0124.cdr.app.service.bithumb.tick.BithumbTickService;
import com.dk0124.cdr.app.service.rest.upbit.UpbitRestRequestService;
import com.dk0124.cdr.app.service.upbit.orderbook.UpbitOrderbookService;
import com.dk0124.cdr.app.service.upbit.tick.UpbitTickService;
import com.dk0124.cdr.app.socket.handler.SocketClientHandlerFactory;
import com.dk0124.cdr.app.socket.handler.WebSocketClientPublisherHandler;
import com.dk0124.cdr.app.socket.socketClient.bithumb.BithumbOrderbookSocketClient;
import com.dk0124.cdr.app.socket.socketClient.bithumb.BithumbTickSocketClient;
import com.dk0124.cdr.app.socket.socketClient.upbit.UpbitOrderbookSocketClient;
import com.dk0124.cdr.app.socket.socketClient.upbit.UpbitTickSocketClient;
import com.dk0124.cdr.constants.Uri;
import com.dk0124.cdr.constants.coinCode.CoinCode;
import com.dk0124.cdr.constants.task.TaskType;
import com.dk0124.cdr.constants.vendor.VendorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
@Setter(AccessLevel.PRIVATE)
@ToString
public class SocketClientBuilder {

    private final SocketClientHandlerFactory socketClientHandlerFactory;
    private final Map<TaskType, AbstractSocketClient> taskSocketMap;

    private final UpbitRestRequestService upbitRestRequestService;
    private final UpbitOrderbookService upbitOrderbookService;
    private final UpbitTickService upbitTickService;

    private final BithumbOrderbookService bithumbOrderbookService;
    private final BithumbTickService bithumbTickService;

    private URI uri;
    private WebSocketClientPublisherHandler socketHandler;
    private TaskType taskType;
    private VendorType vendorType;
    private List<CoinCode> codes;


    public SocketClientBuilder setType(TaskType taskType) {
        this.setTaskType(taskType);
        return this;
    }

    public SocketClientBuilder setVendor(VendorType vendorType) {
        this.setVendorType(vendorType);
        return this;
    }

    public SocketClientBuilder setCoinCodes(List<CoinCode> codes) {
        this.setCodes(codes);
        return this;
    }

    public AbstractSocketClient initialize() {
        if (!isInitializable())
            throw new RuntimeException("missing arg : " + this.toString());
        try {
            this.uri = createUri();
            AbstractSocketClient socketClient = newSocketClient();
            registerTaskMap(socketClient);
            return socketClient;
        } catch (Exception e) {
            log.error("error on initializing handler \n error : {}",e);
            throw new RuntimeException("error on initializing handler ");
        }
    }

    private boolean isInitializable() {
        return (isValidTaskType() && vendorType != null && codes != null);
    }

    private boolean isValidTaskType() {
        return (taskType != null && Arrays.asList(TaskType.values()).contains(taskType));
    }

    private URI createUri() throws URISyntaxException {
        switch (vendorType) {
            case UPBIT:
                return new URI(Uri.UPBIT_SOCKET_URI.getAddress());
            case BITHUMB:
                return new URI(Uri.BITHUMB_SOCKET_URI.getAddress());
            default:
                throw new RuntimeException("invalid vendor ");
        }
    }


    private ListenableFuture<WebSocketSession> newClientSession() {
        return new StandardWebSocketClient().doHandshake(socketHandler, null, uri);
    }

    private AbstractSocketClient newSocketClient() {
        AbstractSocketClient socketClient;

        if (taskType == TaskType.UPBIT_TICK) {
            socketClient = new UpbitTickSocketClient(
                    upbitRestRequestService,
                    uri,
                    socketClientHandlerFactory.getHandler(taskType, vendorType),
                    taskType,
                    vendorType,
                    codes,
                    upbitTickService
            );
        } else if (taskType == TaskType.UPBIT_ORDERBOOK) {
            socketClient = new UpbitOrderbookSocketClient(
                    upbitRestRequestService,
                    uri,
                    socketClientHandlerFactory.getHandler(taskType, vendorType),
                    taskType,
                    vendorType,
                    codes,
                    upbitOrderbookService
            );
        } else if (taskType == TaskType.BITHUMB_TICK) {
            socketClient = new BithumbTickSocketClient(
                    uri,
                    socketClientHandlerFactory.getHandler(taskType, vendorType),
                    taskType,
                    vendorType,
                    codes,
                    bithumbTickService
            );
        }
        else if (taskType == TaskType.BITHUMB_ORDERBOOK) {
            socketClient = new BithumbOrderbookSocketClient(
                    uri,
                    socketClientHandlerFactory.getHandler(taskType, vendorType),
                    taskType,
                    vendorType,
                    codes,
                    bithumbOrderbookService
            );
        }
        else {
            throw new RuntimeException("invalid socket info");
        }
        return socketClient;
    }

    private void registerTaskMap(AbstractSocketClient socketClient) {
        switch (socketClient.getTaskType()) {
            case UPBIT_ORDERBOOK:
                taskSocketMap.put(TaskType.UPBIT_ORDERBOOK, socketClient);
                break;
            case UPBIT_TICK:
                taskSocketMap.put(TaskType.UPBIT_TICK, socketClient);
                break;
            case BITHUMB_TICK:
                taskSocketMap.put(TaskType.BITHUMB_TICK, socketClient);
                break;
            case BITHUMB_ORDERBOOK:
                taskSocketMap.put(TaskType.BITHUMB_ORDERBOOK, socketClient);
                break;
            default:
                throw new RuntimeException("task map match fail");
        }
    }
}
