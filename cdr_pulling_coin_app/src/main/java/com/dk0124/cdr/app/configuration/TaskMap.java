package com.dk0124.cdr.app.configuration;

import com.dk0124.cdr.app.socket.socketClient.AbstractSocketClient;
import com.dk0124.cdr.constants.task.TaskType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class TaskMap {
    @Bean
    public Map<TaskType, AbstractSocketClient> taskSocketMap() {
        ConcurrentHashMap map = new ConcurrentHashMap<TaskType, AbstractSocketClient>();
        return map;
    }
}
