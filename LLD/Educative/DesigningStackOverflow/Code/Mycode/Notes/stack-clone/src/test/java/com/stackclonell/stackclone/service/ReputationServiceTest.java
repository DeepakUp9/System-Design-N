package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.event.PostVotedEvent;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReputationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReputationService reputationService; // This is the Observer

    @Test
    void shouldAwardPlusFiveReputationOnQuestionUpvote() {
        // ARRANGE
        User author = new User();
        author.setId(5L);
        author.setReputation(10L); // Starting reputation

        // Mock the repository call to find the user
        when(userRepository.findById(5L)).thenReturn(Optional.of(author));

        // Create a mock event representing a +1 vote on a Question
        // NOTE: We must mock the event object to ensure it carries the right flags
        PostVotedEvent questionUpvoteEvent = mock(PostVotedEvent.class);
        when(questionUpvoteEvent.getAuthorId()).thenReturn(5L);
        when(questionUpvoteEvent.getVoteValue()).thenReturn(1);
        when(questionUpvoteEvent.isQuestion()).thenReturn(true);

        // ACT: The listener handles the event
        reputationService.handlePostVoted(questionUpvoteEvent);

        // ASSERT: User's reputation should be updated (10 + 5 = 15)
        verify(userRepository).save(author);
        assertEquals(15L, author.getReputation());
    }

    @Test
    void shouldAwardPlusTenReputationOnAnswerUpvote() {
        // ARRANGE
        User author = new User();
        author.setId(5L);
        author.setReputation(10L); // Starting reputation

        when(userRepository.findById(5L)).thenReturn(Optional.of(author));

        // Create a mock event representing a +1 vote on an Answer
        PostVotedEvent answerUpvoteEvent = mock(PostVotedEvent.class);
        when(answerUpvoteEvent.getAuthorId()).thenReturn(5L);
        when(answerUpvoteEvent.getVoteValue()).thenReturn(1);
        when(answerUpvoteEvent.isQuestion()).thenReturn(false); // False means it's an answer

        // ACT: The listener handles the event
        reputationService.handlePostVoted(answerUpvoteEvent);

        // ASSERT: User's reputation should be updated (10 + 10 = 20)
        verify(userRepository).save(author);
        assertEquals(20L, author.getReputation());
    }

    // CRITICAL: Test case for downvotes or invalid votes (Edge Case/Resilience)
    @Test
    void shouldNotAwardReputationOnDownvote() {
        // ARRANGE
        User author = new User();
        author.setId(5L);
        author.setReputation(10L);

        when(userRepository.findById(5L)).thenReturn(Optional.of(author));

        // Create a mock event representing a -1 vote
        PostVotedEvent downvoteEvent = mock(PostVotedEvent.class);
        when(downvoteEvent.getAuthorId()).thenReturn(5L);
        when(downvoteEvent.getVoteValue()).thenReturn(-1); // Downvote
        when(downvoteEvent.isQuestion()).thenReturn(true);

        // ACT
        reputationService.handlePostVoted(downvoteEvent);

        // ASSERT: Rep code is simplified to only handle positive voteValue, so reputation should be unchanged.
        verify(userRepository).save(author); // Save still happens, but rep is the same
        assertEquals(10L, author.getReputation());
    }
}