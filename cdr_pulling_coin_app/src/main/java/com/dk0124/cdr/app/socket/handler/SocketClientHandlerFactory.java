package com.dk0124.cdr.app.socket.handler;


import com.dk0124.cdr.app.socket.handler.bithumb.BithumbOrderbookSocketClientHandler;
import com.dk0124.cdr.app.socket.handler.bithumb.BithumbTickSocketClientHandler;
import com.dk0124.cdr.app.socket.handler.upbit.UpbitOrderbookSocketClientHandler;
import com.dk0124.cdr.app.socket.handler.upbit.UpbitTickSocketClientHandler;
import com.dk0124.cdr.constants.task.TaskType;
import com.dk0124.cdr.constants.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketClientHandlerFactory {
    private final UpbitOrderbookSocketClientHandler upbitOrderBookSocketClientHandler;
    private final UpbitTickSocketClientHandler upbitTickSocketClientHandler;
    private final BithumbTickSocketClientHandler bithumbTickSocketClientHandler;
    private final BithumbOrderbookSocketClientHandler bithumbOrderbookSocketClientHandler;


    public WebSocketClientPublisherHandler getHandler(TaskType taskType, VendorType vendorType){
        if(taskType == TaskType.UPBIT_ORDERBOOK && vendorType == VendorType.UPBIT){
            return upbitOrderBookSocketClientHandler;
        }
        else if(taskType == TaskType.UPBIT_TICK && vendorType == VendorType.UPBIT){
            return upbitTickSocketClientHandler;
        }
        else if(taskType == TaskType.BITHUMB_TICK && vendorType == VendorType.BITHUMB){
            return bithumbTickSocketClientHandler;
        }
        else if(taskType == TaskType.BITHUMB_ORDERBOOK && vendorType == VendorType.BITHUMB){
            return bithumbOrderbookSocketClientHandler;
        }
        else{
            throw new RuntimeException("invalid arg : "+ taskType +" , " + vendorType );

        }

    }
}
