package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BasicLongPollingClient {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String SERVER_URL = "http://localhost:8080/poll";
    private static final String UPDATE_URL = "http://localhost:8080/update";

    public static void main(String[] args) throws IOException, InterruptedException {
        // Start a thread to simulate message updates
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                updateMessage("Message 1");
                Thread.sleep(3000);
                updateMessage("Message 2");
                Thread.sleep(4000);
                updateMessage("Message 3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Continuous polling
        while (true) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .timeout(Duration.ofSeconds(120))
                    .version(HttpClient.Version.HTTP_1_1) // or HTTP_2 keep-alive
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Received: " + response.body());
            } catch (IOException | InterruptedException e) {
                System.out.println("Request failed: " + e.getMessage());
                Thread.sleep(1000); // wait before retrying
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