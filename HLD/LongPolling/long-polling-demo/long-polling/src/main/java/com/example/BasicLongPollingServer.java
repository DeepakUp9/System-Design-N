package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class BasicLongPollingServer {
    private static String latestMessage = "Initial message";
    private static long lastUpdateTime = System.currentTimeMillis();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/poll", new PollHandler());
        server.createContext("/update", new UpdateHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class PollHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            long startTime = System.currentTimeMillis();
            long timeout = TimeUnit.SECONDS.toMillis(30); // 30-second timeout

            // Wait until message changes or timeout
            while (System.currentTimeMillis() - startTime < timeout) {
                synchronized (BasicLongPollingServer.class) {
                    if (System.currentTimeMillis() - lastUpdateTime > 1000) { // if message was updated more than 1 second ago
                        break;
                    }
                }
                try {
                    Thread.sleep(100); // sleep for 100ms between checks
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            String response;
            synchronized (BasicLongPollingServer.class) {
                response = latestMessage;
            }

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class UpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                return;
            }

            String newMessage = new String(exchange.getRequestBody().readAllBytes());
            synchronized (BasicLongPollingServer.class) {
                latestMessage = newMessage;
                lastUpdateTime = System.currentTimeMillis();
            }

            String response = "Message updated";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}