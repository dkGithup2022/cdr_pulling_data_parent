package com.dk0124.cdr.app.socket.pubsub;


public interface SocketClientOnFailPublisher {
    void setSubscriber(SocketClientOnFailSubscriber subscriber);
    void notifySubscriber(String key);
}
