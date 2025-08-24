package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class IntermediateLongPollingServer {
    private static final ConcurrentHashMap<Long, String> messages = new ConcurrentHashMap<>();
    private static final AtomicLong messageId = new AtomicLong(0);
    private static final AtomicLong lastMessageId = new AtomicLong(-1);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/poll", new PollHandler());
        server.createContext("/update", new UpdateHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("Intermediate Server started on port 8080");
    }

    static class PollHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String headerValue = exchange.getRequestHeaders().getFirst("Last-Message-ID");
            long clientLastId = Long.parseLong(headerValue != null ? headerValue : "-1");
            long timeout = 30000; // 30 seconds
            long startTime = System.currentTimeMillis();

            // Wait for new message or timeout
            while (System.currentTimeMillis() - startTime < timeout) {
                long currentLastId = lastMessageId.get();
                if (currentLastId > clientLastId) {
                    String response = messages.get(currentLastId);
                    sendResponse(exchange, 200, response);
                    return;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Timeout reached
            sendResponse(exchange, 204, "No new messages");
        }
    }

    static class UpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String message = new String(exchange.getRequestBody().readAllBytes());
            long newId = messageId.incrementAndGet();
            messages.put(newId, message);
            lastMessageId.set(newId);

            sendResponse(exchange, 200, "Message " + newId + " added");
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}