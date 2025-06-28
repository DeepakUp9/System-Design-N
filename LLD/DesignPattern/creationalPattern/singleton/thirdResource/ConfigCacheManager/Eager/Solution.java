package LLD.DesignPattern.creationalPattern.singleton.thirdResource.ConfigCacheManager.Eager;


import java.util.concurrent.ConcurrentHashMap;

class ConfigCacheManager {

    private static volatile ConfigCacheManager instance;

    // Local in-memory cache
    private final ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();

    private ConfigCacheManager() {
        // Subscribe to Redis Pub/Sub for config changes
        ConfigUpdateListener.subscribeToUpdates();
    }

    public static ConfigCacheManager getInstance() {
        if (instance == null) {
            synchronized (ConfigCacheManager.class) {
                if (instance == null) {
                    instance = new ConfigCacheManager();
                }
            }
        }
        return instance;
    }

    public String getConfig(String key) {
        return configMap.get(key);
    }

    public void updateConfig(String key, String value) {
        configMap.put(key, value);
        RedisPublisher.publishConfigUpdate(key);
    }

    public void refreshConfig(String key, String value) {
        configMap.put(key, value);
        System.out.println("Refreshed local config: " + key + " = " + value);
    }
}

class RedisPublisher {

    public static void publishConfigUpdate(String key) {
        System.out.println("Published update for key: " + key);
        // Simulate notifying Redis and other pods
        // In real case, this would be a Redis `PUBLISH` command
    }
}

class ConfigUpdateListener {

    public static void subscribeToUpdates() {
        // Simulate Redis listener with one-time mock message
        new Thread(() -> {
            try {
                // Simulate delay for receiving message
                Thread.sleep(3000);
                String updatedKey = waitForMessageFromRedis();
                String newValue = fetchFromRedis(updatedKey);
                ConfigCacheManager.getInstance().refreshConfig(updatedKey, newValue);
                System.out.println("Updated local config for key: " + updatedKey);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Redis-Listener-Thread").start();
    }

    private static String waitForMessageFromRedis() {
        return "currency";
    }

    private static String fetchFromRedis(String key) {
        return "INR";
    }
}

public class Solution {
    public static void main(String[] args) throws InterruptedException {

        ConfigCacheManager cacheManager = ConfigCacheManager.getInstance();

        // Initial config load
         cacheManager.updateConfig("currency", "USD");

        // Simulate threads accessing config
        Runnable readTask = () -> {
            String configValue = cacheManager.getConfig("currency");
            System.out.println(Thread.currentThread().getName() + " read config: " + configValue);
        };

        Thread t1 = new Thread(readTask, "Service-1");
         Thread t2 = new Thread(readTask, "Service-2");

         t1.start();
         t2.start();
         t1.join();
         t2.join();

         // Simulate delay for external update to be received via RedisListener thread
         Thread.sleep(4000);

         // Access config again after simulated update
         Thread t3 = new Thread(readTask, "Service-3");
         t3.start();
         t3.join();
    }
}
