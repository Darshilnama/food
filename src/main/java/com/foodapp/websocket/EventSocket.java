package com.foodapp.websocket;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/events/{userId}")
public class EventSocket {

    @Inject
    private EventBroadcaster broadcaster;

    private EventBroadcaster getBroadcaster() {
        if (broadcaster == null) {
            try {
                broadcaster = CDI.current().select(EventBroadcaster.class).get();
            } catch (Exception e) {
                // Ignore if CDI is not available
            }
        }
        return broadcaster;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        EventBroadcaster b = getBroadcaster();
        if (b != null) {
            b.addSession(userId, session);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") Long userId) {
        EventBroadcaster b = getBroadcaster();
        if (b != null) {
            b.removeSession(userId, session);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Log error
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        // Not used by clients, server pushes only
    }
}
