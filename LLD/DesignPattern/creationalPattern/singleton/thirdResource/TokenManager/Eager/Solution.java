package LLD.DesignPattern.creationalPattern.singleton.thirdResource.TokenManager.Eager;


import java.time.Instant;

 class TokenManager {

    private static volatile TokenManager instance;
    private String bearerToken;
    private Instant expiryTime;

    // Private constructor
    private TokenManager() {
    }

    // Double-checked locking singleton
    public static TokenManager getInstance() {
        if (instance == null) {
            synchronized (TokenManager.class) {
                if (instance == null) {
                    instance = new TokenManager();
                }
            }
        }
        return instance;
    }

    // Thread-safe method to get token
    public String getToken() {
        if (isTokenValid()) {
            return bearerToken;
        }

        synchronized (this) {
            if (!isTokenValid()) {
                refreshTokenFromExternalService();
            }
            return bearerToken;
        }
    }

    // Check token validity with a buffer (e.g., 60 seconds before expiry)
    private boolean isTokenValid() {
        return bearerToken != null && expiryTime != null &&
                Instant.now().isBefore(expiryTime.minusSeconds(60));
    }

    // Simulated token refresh from external service
    private void refreshTokenFromExternalService() {
        // Simulate API call
        this.bearerToken = "Bearer abc12345"; // In real code, make HTTP request
        this.expiryTime = Instant.now().plusSeconds(300); // Token valid for 5 min
        System.out.println("Token refreshed at: " + Instant.now());
    }
}


class PaymentProcessor {

    public void process() {
        TokenManager tokenManager = TokenManager.getInstance();
        String token = tokenManager.getToken();

        // Use token to make external API call
        System.out.println("Using token: " + token);
    }

    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        processor.process();
    }
}
