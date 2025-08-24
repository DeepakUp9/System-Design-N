package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class IntermediateLongPollingClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String SERVER_URL = "http://localhost:8080/poll";
    private static final String UPDATE_URL = "http://localhost:8080/update";
    private static long lastReceivedId = -1;

    public static void main(String[] args) {
        // Message updater thread
        new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(2000 + (i * 1000));
                    updateMessage("Intermediate Message " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Polling loop
        while (true) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(SERVER_URL))
                        .header("Last-Message-ID", String.valueOf(lastReceivedId))
                        .timeout(Duration.ofSeconds(35))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String message = response.body();
                    System.out.println("Received: " + message);
                    // In a real app, we'd parse the ID from the message or headers
                    lastReceivedId++;
                } else if (response.statusCode() == 204) {
                    System.out.println("No new messages");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Error in polling: " + e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private static void updateMessage(String message) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UPDATE_URL))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Update response: " + response.body());
    }
}