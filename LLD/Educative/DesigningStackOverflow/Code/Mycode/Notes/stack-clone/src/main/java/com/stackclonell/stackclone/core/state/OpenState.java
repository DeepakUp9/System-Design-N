package com.stackclonell.stackclone.core.state;

import com.stackclonell.stackclone.core.event.PostVotedEvent;
import com.stackclonell.stackclone.core.model.Post;
import com.stackclonell.stackclone.core.model.PostStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * LLD: Concrete State 1. Allows full functionality.
 */
@Component
public class OpenState implements PostState {

    // LLD & Spring Integration: Inject the publisher for the Observer pattern
    private final ApplicationEventPublisher eventPublisher;

    // Spring injects the publisher automatically
    public OpenState(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handleVote(Post post, int voteValue) {
        // Production Logic: Check if user has minimum reputation to vote
        // For now, simple score update
        post.setScore(post.getScore() + voteValue);

        // 2. LLD: Publish the Event (Observer Pattern Trigger)
        // Decoupled action: The PostState doesn't care who listens or what they do.
        PostVotedEvent event = new PostVotedEvent(post, voteValue);
        eventPublisher.publishEvent(event);

        // After updating score, we need to trigger an event (Observer Pattern - Step 4)
        System.out.println(
                String.format("Post %d is OPEN. Score updated to %d. (Observer event pending)",
                        post.getId(),
                        post.getScore()
                ));
    }

    @Override
    public void handleClose(Post post) {
        // Production Logic: Only high-rep users can close posts
        System.out.println(String.format("Post %d closed by a moderator.", post.getId()));
        post.setStatus(PostStatus.CLOSED);

        // Transition the state of the Context object
        // NOTE: The PostStateFactory (coming up) will inject the ClosedState object.
        // For simplicity now, we assume state injection.
    }
}