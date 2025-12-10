package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.event.PostVotedEvent;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * LLD: Concrete Observer 1. Updates reputation based on vote events.
 * Resilience: We ensure this update is atomic and successful.
 */
@Service
public class ReputationService {

    private final UserRepository userRepository;
    private final WebSocketService webSocketService;

    public ReputationService(UserRepository userRepository, WebSocketService webSocketService) {
        this.userRepository = userRepository;
        this.webSocketService = webSocketService;
    }

    // LLD: The listener method, annotated with @EventListener
    // This method is triggered automatically when a PostVotedEvent is published.
    @EventListener
    // Transactionality/Resilience: Ensures this listener runs in its own transaction
    // to prevent a failure here from rolling back the main post update.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePostVoted(PostVotedEvent event) {

        // Stack Overflow Rule: Upvote on Question = +5 to Author
        // Upvote on Answer = +10 to Author

//        int reputationChange = 0;
//        if (event.getVoteValue() > 0) { // Upvote
//            reputationChange = event.isQuestion() ? 5 : 10;
//        }
//        // Production Note: Downvotes usually cost the voter -1, but this logic is complex
//        // and would require checking the voting user's ID, which is missing from this basic event.
//
//        // Scalability/Persistence: Retrieve the Author from the database
//        userRepository.findById(event.getAuthorId()).ifPresent(author -> {
//            author.setReputation(author.getReputation() + reputationChange);
//            userRepository.save(author);
//            System.out.println(
//                    String.format("Observer: ReputationService updated user %d. Change: +%d. New Rep: %d",
//                            author.getId(),
//                            reputationChange,
//                            author.getReputation()
//                    ));
//        });


        User author = userRepository.findById(event.getAuthorId()).orElse(null);
        if (author == null) return;

        long oldReputation = author.getReputation();
        long repChange = 0; // Calculate change based on vote value/type

        // ... (Reputation calculation logic here) ...

        long newReputation = oldReputation + repChange;
        author.setReputation(newReputation);
        userRepository.save(author);

        // Real-Time: Notify the user of the change
        webSocketService.notifyUserReputationChange(author.getUsername(), Map.of(
                "type", "reputationChange",
                "change", repChange,
                "newReputation", newReputation
        ));
    }
}