package com.dk0124.cdr.app.socket.pubsub;

public interface SocketClientOnFailSubscriber {
    public void notified(String key);
}
