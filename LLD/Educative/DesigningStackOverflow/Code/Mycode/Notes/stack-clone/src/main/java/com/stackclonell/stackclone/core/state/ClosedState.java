package com.stackclonell.stackclone.core.state;

import com.stackclonell.stackclone.core.model.Post;
import org.springframework.stereotype.Component;
import com.stackclonell.stackclone.service.CustomMetricService; // NEW IMPORT
/**
 * LLD: Concrete State 2. Restricts functionality.
 */
@Component
public class ClosedState implements PostState {

    // Inject the metric service via the constructor in the factory
    private final CustomMetricService metricService;

    public ClosedState(CustomMetricService metricService) {
        this.metricService = metricService;
        // Increment the custom counter upon instantiation of the closed state
        metricService.incrementClosedQuestionCounter();
    }

    public  ClosedState(){

    }


    @Override
    public void handleVote(Post post, int voteValue) {
        // Edge Case Handling & Resilience: Prevent voting on closed posts
        System.out.println(
                String.format("Post %d is CLOSED. Vote rejected. No change to score %d.",
                        post.getId(),
                        post.getScore()
                ));
        // A real-world app would throw a custom business exception here.
        // will throw exceptions later in this class, mabe local exception or global exception
    }

    @Override
    public void handleClose(Post post) {
        // Edge Case Handling: Cannot close a post that is already closed
        System.out.println(String.format("Post %d is already CLOSED.", post.getId()));
    }
}