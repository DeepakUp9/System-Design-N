package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.model.Post;
import com.stackclonell.stackclone.core.state.PostStateFactory;
import com.stackclonell.stackclone.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Service layer handles the application logic and acts as the entry point
 * for the State Pattern execution.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostStateFactory stateFactory;

    public PostService(PostRepository postRepository, PostStateFactory stateFactory) {
        this.postRepository = postRepository;
        this.stateFactory = stateFactory;
    }

    /**
     * LLD: Handles the request to vote on a post.
     * @param postId The ID of the post.
     * @param voteValue +1 or -1
     */
    @Transactional // Ensures atomicity (Resilience/Consistency)
    public void voteOnPost(Long postId, int voteValue) {
        // 1. Get the Context Entity (Post)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 2. LLD: Inject the correct State Object into the Context
        // The Post's current status (OPEN, CLOSED, etc.) determines the logic executed.
        post.setCurrentState(stateFactory.getState(post.getStatus()));

        // 3. LLD: Delegate the behavior to the State Object
        post.vote(voteValue);

        // 4. Save the updated Post Context (with new score/status)
        postRepository.save(post);
    }
}