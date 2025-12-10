package com.stackclonell.stackclone.web;

import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.service.FeatureFlagService;
import com.stackclonell.stackclone.service.QuestionService;
import com.stackclonell.stackclone.service.PostService; // Used for voting
import com.stackclonell.stackclone.repository.UserRepository; // Placeholder for finding current user
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page; // Use Page for return type
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final PostService postService; // Used for delegated voting logic
    private final UserRepository userRepository; // Production: Replace with dedicated Auth/User Service

    private final FeatureFlagService featureFlagService; // New dependency


    public QuestionController(QuestionService questionService, PostService postService, UserRepository userRepository, FeatureFlagService featureFlagService) {
        this.questionService = questionService;
        this.postService = postService;
        this.userRepository = userRepository;
        this.featureFlagService = featureFlagService;
    }

    /**
     * Endpoint to fetch all questions.
     * LLD: Integrates the Strategy Pattern via the 'sort' parameter.
     * @param sortParam The query parameter to select the sorting strategy ("newest", "votes").
     */
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions(
            @RequestParam(name = "sort", defaultValue = "newest") String sortParam) {

        List<Question> questions = questionService.getAllQuestions(sortParam);
        return ResponseEntity.ok(questions);
    }

    /**
     * Endpoint to fetch all questions with Pagination and Sorting Strategy.
     * @param sortParam Strategy key ("newest", "votes").
     * @param page Pagination parameter (default 0).
     * @param size Pagination parameter (default 10).
     */
    @GetMapping
    public ResponseEntity<Page<Question>> getAllQuestions(
            @RequestParam(name = "sort", defaultValue = "newest") String sortParam,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // Input Validation (Resilience): Ensure page/size are reasonable
        if (size < 1 || size > 100) {
            size = 10;
        }

        Page<Question> questionPage = questionService.getAllQuestions(sortParam, page, size);

        // API response includes pagination metadata (total pages, total elements)
        return ResponseEntity.ok(questionPage);
    }

    /**
     * Endpoint to fetch, search, filter, and sort questions with Pagination.
     * Strategy: 'sort' parameter determines the ranking algorithm (Votes, Newest).
     * Scalability: Uses Pageable and DB indexing.
     * @param sortParam Strategy key.
     * @param keyword Optional keyword for title search.
     * @param tag Optional tag for filtering.
     * @param page Page number.
     * @param size Page size.
     */
    @GetMapping
    public ResponseEntity<Page<Question>> getAllQuestions(
            @RequestParam(name = "sort", defaultValue = "newest") String sortParam,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // Input Validation (Resilience): Enforce max page size
        if (size < 1 || size > 100) {
            size = 10;
        }

        Page<Question> questionPage = questionService.searchAndSortQuestions(
                sortParam, keyword, tag, page, size);

        return ResponseEntity.ok(questionPage);
    }

    /**
     * Endpoint to create a new question. Requires authentication (from SecurityConfig).
     */
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question, Authentication authentication) {
        // Production: Use Authentication principal to find the logged-in User
        // Simplified User lookup for demonstration:
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

        question.setAuthor(currentUser);
        question.setCreationDate(LocalDateTime.now());
        // LLD: The QuestionService handles the persistence.
        Question createdQuestion = questionService.saveQuestion(question);

        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    /**
     * Endpoint to handle voting on a Question.
     * LLD: Triggers the State and Observer Patterns in the PostService.
     */
    @PostMapping("/{questionId}/vote")
    public ResponseEntity<Void> voteQuestion(@PathVariable Long questionId,
                                             @RequestParam int voteValue,
                                             Authentication authentication) {

        // Security/LLD Check: Ensure voteValue is valid (+1 or -1)
        if (voteValue != 1 && voteValue != -1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Production Logic: Check if user has sufficient reputation to vote
        // (This check usually belongs in a RepCheckService called by PostService)

        // LLD: Delegate the logic to the PostService (which handles State/Observer)
        postService.voteOnPost(questionId, voteValue);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * [GET] Endpoint to determine which UI version the client should load.
     * This is the core A/B test decision point.
     */
    @GetMapping("/config/sorting-ui")
    public ResponseEntity<String> getSortingUiVersion(Authentication authentication) {

        Optional<User> currentUser = Optional.empty();
        if (authentication != null) {
            currentUser = userRepository.findByUsername(authentication.getName());
        }

        if (featureFlagService.isNewSortingUiEnabled(currentUser)) {
            // A/B Test Group B: New UI version (e.g., Infinite scroll, new buttons)
            return ResponseEntity.ok("V2_NEW_UI");
        } else {
            // A/B Test Group A: Original UI version (e.g., Standard pagination)
            return ResponseEntity.ok("V1_OLD_UI");
        }
    }
}