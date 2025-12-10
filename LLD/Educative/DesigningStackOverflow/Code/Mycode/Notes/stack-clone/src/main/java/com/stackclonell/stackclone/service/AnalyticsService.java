package com.stackclonell.stackclone.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service to handle heavy, non-critical computations asynchronously.
 * This offloads work from the main thread (Scalability).
 */
@Service
public class AnalyticsService {

    // Simulate a dependency that fetches complex, large data
    // private final BigDataRepository bigDataRepository;

    /**
     * Executes a task asynchronously and returns the result wrapped in a CompletableFuture.
     * This allows the caller to continue processing and retrieve the result later.
     * @param userId The ID of the user whose data is being analyzed.
     * @return A CompletableFuture holding the calculated metric.
     */
    @Async("getAsyncExecutor") // Specify the executor defined in AsyncConfig
    public CompletableFuture<Double> calculateUserInfluenceScore(Long userId) {

        System.out.println("Async Task Started: Calculating influence score for user " + userId + " on thread: " + Thread.currentThread().getName());

        // 1. Simulate a long-running, CPU-bound operation (e.g., complex reputation score calculation)
        try {
            Thread.sleep(5000);

            // Hypothetical calculation based on log data, votes, and time
            double score = Math.random() * 1000 + (userId * 10);

            System.out.println("Async Task Finished: Score calculated successfully.");

            // 2. Wrap the result in CompletableFuture.completedFuture()
            return CompletableFuture.completedFuture(score);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 3. Handle errors by wrapping an exception
            return CompletableFuture.failedFuture(new RuntimeException("Analytics calculation interrupted.", e));
        }
    }
}