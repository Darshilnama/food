package com.foodapp.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ApplicationScoped
public class EventBroadcaster {
    
    private final Map<Long, Set<Session>> userSessions = new ConcurrentHashMap<>();
    private final Set<Session> allSessions = new CopyOnWriteArraySet<>();

    public void addSession(Long userId, Session session) {
        userSessions.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(session);
        allSessions.add(session);
    }

    public void removeSession(Long userId, Session session) {
        Set<Session> sessions = userSessions.get(userId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                userSessions.remove(userId);
            }
        }
        allSessions.remove(session);
    }

    public void broadcastToUser(Long userId, String eventPayload) {
        Set<Session> sessions = userSessions.get(userId);
        if (sessions != null) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(eventPayload);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void broadcastToAll(String eventPayload) {
        for (Session session : allSessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(eventPayload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
