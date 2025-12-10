package com.stackclonell.stackclone.core.state;

import com.stackclonell.stackclone.core.model.Post;

/**
 * LLD: The State Interface. Defines a common interface for all concrete states.
 * This decouples the state-specific behavior from the Post entity itself.
 */
public interface PostState {

    /**
     * Handles the voting logic. This logic will vary based on the current state.
     * @param post The context post being voted on.
     * @param voteValue The value of the vote (+1 or -1).
     */
    void handleVote(Post post, int voteValue);

    /**
     * Handles the action of closing a post.
     * @param post The context post to be closed.
     */
    void handleClose(Post post);

    // Add other common behaviors here, e.g., handleEdit(), handleComment()
}