package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.model.Comment;
import com.stackclonell.stackclone.core.model.Post;
import com.stackclonell.stackclone.repository.CommentRepository;
import com.stackclonell.stackclone.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FeatureFlagService featureFlagService; // New dependency

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, FeatureFlagService featureFlagService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.featureFlagService = featureFlagService;
    }

    /**
     * Finds the parent post, sets metadata, and saves the new comment.
     */
    public Comment saveComment(Long postId, Comment comment) {
        // Resilience: Check the kill switch flag
        if (!featureFlagService.isCommentsAllowed()) {
            throw new IllegalStateException("Comment posting is temporarily disabled due to a maintenance issue.");
        }
        
        Post parentPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post (Question or Answer) not found with ID: " + postId));


        comment.setParentPost(parentPost);
        comment.setCreatedDate(LocalDateTime.now());

        // Production Note: We would publish a CommentAddedEvent here to trigger
        // the NotificationService Observer (like we did for New Answers).

        return commentRepository.save(comment);
    }

    /**
     * Fetches paginated comments for a specific post.
     */
    public Page<Comment> getCommentsForPost(Long postId, Pageable pageable) {
        // Find by Post ID and return a paginated result set
        return commentRepository.findByParentPostIdOrderByCreatedDateAsc(postId, pageable);
    }
}