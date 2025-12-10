package com.stackclonell.stackclone.web;

import com.stackclonell.stackclone.core.model.Comment;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.UserRepository;
import com.stackclonell.stackclone.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
// RESTful design: /api/posts/{postId}/comments applies to both Q's and A's
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    public CommentController(CommentService commentService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    /**
     * [POST] Creates a new comment on the specified Post.
     * Requires authentication (Security).
     */
    @PostMapping
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @RequestBody Comment comment,
            Authentication authentication) {

        // Security: Get the authenticated user (author)
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

        comment.setAuthor(currentUser);

        Comment createdComment = commentService.saveComment(postId, comment);

        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * [GET] Retrieves paginated comments for the specified Post.
     * (Core Feature / Scalability)
     */
    @GetMapping
    public ResponseEntity<Page<Comment>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Scalability: Create the Pageable object
        PageRequest pageable = PageRequest.of(page, size);

        Page<Comment> comments = commentService.getCommentsForPost(postId, pageable);

        return ResponseEntity.ok(comments);
    }
}