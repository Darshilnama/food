package com.foodapp.swing.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class SyncClient {

    private static final String WS_URL = "ws://localhost:8080/FoodDeliveryApp/ws/events/";
    private WebSocket webSocket;

    public void connect(Long userId, Consumer<String> onEvent) {
        HttpClient client = HttpClient.newHttpClient();
        
        client.newWebSocketBuilder()
              .buildAsync(URI.create(WS_URL + userId), new WebSocket.Listener() {
                  @Override
                  public void onOpen(WebSocket webSocket) {
                      System.out.println("Connected to real-time events");
                      WebSocket.Listener.super.onOpen(webSocket);
                  }

                  @Override
                  public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                      onEvent.accept(data.toString());
                      return WebSocket.Listener.super.onText(webSocket, data, last);
                  }

                  @Override
                  public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                      System.out.println("Disconnected: " + reason);
                      return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                  }
                  
                  @Override
                  public void onError(WebSocket webSocket, Throwable error) {
                      System.err.println("WebSocket error: " + error.getMessage());
                      WebSocket.Listener.super.onError(webSocket, error);
                  }
              }).thenAccept(ws -> this.webSocket = ws);
    }
    
    public void disconnect() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Application shutting down");
        }
    }
}
