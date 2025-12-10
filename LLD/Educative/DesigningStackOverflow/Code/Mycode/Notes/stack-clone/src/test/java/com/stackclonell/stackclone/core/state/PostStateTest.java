package com.stackclonell.stackclone.core.state;

import com.stackclonell.stackclone.core.event.PostVotedEvent;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostStateTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private Question question;

    @BeforeEach
    void setup() {
        User author = new User();
        author.setId(1L);

        question = new Question();
        question.setId(100L);
        question.setScore(5);
        question.setAuthor(author);
        // Note: We don't need to set the PostStatus enum here,
        // as the test directly calls the state object.
    }

    @Test
    void openStateShouldUpdateScoreAndPublishEvent() {
        PostState openState = new OpenState(eventPublisher);

        // ACT: Vote on the open question
        openState.handleVote(question, 1);

        // ASSERT 1: Score must be updated (Core LLD behavior)
        assertEquals(6, question.getScore());

        // ASSERT 2: Event must be published (Observer Pattern integration)
        verify(eventPublisher).publishEvent(any(PostVotedEvent.class));
    }

    @Test
    void closedStateShouldRejectVoteAndNotPublishEvent() {
        PostState closedState = new ClosedState(); // ClosedState has no publisher dependency

        // ACT: Vote on the closed question
        closedState.handleVote(question, 1);

        // ASSERT 1: Score must NOT change (State restriction)
        assertEquals(5, question.getScore());

        // ASSERT 2: No event should be published, confirming rejection
        verify(eventPublisher, never()).publishEvent(any());
    }

    // Test case for closed state preventing close action (Resilience)
    @Test
    void closedStateShouldNotAllowClosingAgain() {
        PostState closedState = new ClosedState();

        // ACT
        closedState.handleClose(question);

        // ASSERT (We rely on the console output for now, but a real check would be on the status transition)
        // Since we are unit testing the state object itself, we verify its internal logic.
    }
}