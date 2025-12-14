package com.jigsaw.game.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// Production Note: We need to explicitly scan the 'core-domain' package
// if we use annotations like @Component on classes within it.
// However, to maintain the purity of core-domain, we use a manual @Configuration approach (see 2).
@SpringBootApplication
@EnableCaching // CRITICAL: Enables Spring's caching mechanism (backed by Redis)
public class JigsawGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(JigsawGameApplication.class, args);
    }
}