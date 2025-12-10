package com.stackclonell.stackclone.web;

import com.stackclonell.stackclone.service.AnalyticsService;
import com.stackclonell.stackclone.service.UserService;
import com.stackclonell.stackclone.web.dto.UserProfileDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AnalyticsService analyticsService; // New dependency
    public UserController(UserService userService, AnalyticsService analyticsService) {
        this.userService = userService;
        this.analyticsService = analyticsService;
    }

    /**
     * [GET] Retrieves a comprehensive user profile with paginated activity history.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @PathVariable Long userId,
            // Pagination for questions list
            @RequestParam(defaultValue = "0") int qPage,
            @RequestParam(defaultValue = "5") int qSize,
            // Pagination for answers list
            @RequestParam(defaultValue = "0") int aPage,
            @RequestParam(defaultValue = "5") int aSize) {

        // Scalability: Separate Pageable objects for questions and answers
        PageRequest questionPageable = PageRequest.of(qPage, qSize);
        PageRequest answerPageable = PageRequest.of(aPage, aSize);

        UserProfileDto profile = userService.getUserProfile(userId, questionPageable, answerPageable);

        return ResponseEntity.ok(profile);
    }

    /**
     * [GET] Triggers a heavy asynchronous task and waits for the result (Blocking Demo).
     * NOTE: For real web apps, clients usually poll a status endpoint or use WebSockets,
     * but this demonstrates handling the CompletableFuture result.
     */
    @GetMapping("/{userId}/influence-score")
    public ResponseEntity<String> getInfluenceScore(@PathVariable Long userId) {

        // 1. Trigger the heavy task asynchronously
        CompletableFuture<Double> scoreFuture = analyticsService.calculateUserInfluenceScore(userId);

        // 2. Immediate Response (The Non-Blocking, Scalable way):
        // return new ResponseEntity<>("Calculation started. Check back later.", HttpStatus.ACCEPTED);

        // 3. Demonstrating Future Retrieval (The Blocking Demo):
        try {
            // Wait up to 10 seconds for the result (Resilience: prevents infinite waiting)
            Double score = scoreFuture.get(10, java.util.concurrent.TimeUnit.SECONDS);
            return ResponseEntity.ok("User Influence Score: " + String.format("%.2f", score));

        } catch (java.util.concurrent.TimeoutException e) {
            // Task took too long
            return new ResponseEntity<>("Calculation is still running. Try again in a moment.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // Handle execution errors
            return new ResponseEntity<>("Error calculating score: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}