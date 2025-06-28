package LLD.DesignPattern.creationalPattern.singleton.thirdResource.CredentialManager.Eager;


import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
class CredentialManager {

    private static volatile CredentialManager instance;
    private volatile String token;
    private volatile Instant expiryTime;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ReentrantLock refreshLock = new ReentrantLock();

    private CredentialManager() {
        // Fetch initial token on startup
        refreshToken();

        // Schedule refresh every 30 minutes
        scheduler.scheduleAtFixedRate(this::refreshToken, 30, 30, TimeUnit.MINUTES);
    }

    public static CredentialManager getInstance() {
        if (instance == null) {
            synchronized (CredentialManager.class) {
                if (instance == null) {
                    instance = new CredentialManager();
                }
            }
        }
        return instance;
    }

    public String getToken() {
        // If token is stale or null, refresh (fallback in case scheduler failed)
        if (token == null || Instant.now().isAfter(expiryTime)) {
            try {
                if (refreshLock.tryLock(5, TimeUnit.SECONDS)) {
                    try {
                        // Double-check
                        if (token == null || Instant.now().isAfter(expiryTime)) {
                            refreshToken();
                        }
                    } finally {
                        refreshLock.unlock();
                    }
                } else {
                    // If we can't lock, wait a bit and assume another thread is refreshing
                    System.out.println("Waiting for token to be refreshed by another thread...");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return token;
    }

    private void refreshToken() {
        if (!refreshLock.tryLock()) {
            // Avoid blocking scheduler if refresh is already happening
            return;
        }

        try {
            System.out.println("Refreshing token at " + Instant.now());

            // Simulate token fetch from partner service
            this.token = "token-" + System.currentTimeMillis();

            // Set expiry to 30 minutes later
            this.expiryTime = Instant.now().plusSeconds(30 * 60);

        } finally {
            refreshLock.unlock();
        }
    }
}




public class Solution {
    public static void main(String[] args) throws InterruptedException {
        CredentialManager manager = CredentialManager.getInstance();

        // Simulate 5 threads making API calls
        Runnable task = () -> {
            String token = manager.getToken();
            System.out.println(Thread.currentThread().getName() + " got token: " + token);
        };

        for (int i = 0; i < 5; i++) {
            new Thread(task, "API-Caller-" + i).start();
        }

        // Keep app running to allow scheduled refresh
        Thread.sleep(5 * 60 * 1000); // 5 minutes
    }
}



