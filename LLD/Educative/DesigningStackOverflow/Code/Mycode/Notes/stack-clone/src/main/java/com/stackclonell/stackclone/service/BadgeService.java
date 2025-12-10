package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.event.PostVotedEvent;
import com.stackclonell.stackclone.core.event.AnswerSubmittedEvent;
import com.stackclonell.stackclone.core.model.Badge;
import com.stackclonell.stackclone.core.model.BadgeRank;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.BadgeRepository;
import com.stackclonell.stackclone.repository.QuestionRepository;
import com.stackclonell.stackclone.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * LLD: Observer triggered after a user earns reputation (PostVotedEvent).
     */
    @EventListener
    @Transactional
    public void handleReputationChange(PostVotedEvent event) {
        User user = userRepository.findById(event.getAuthorId()).orElse(null);
        if (user == null) return;

        // Example 1: Reputation-based badge check
        checkReputationBadges(user);
    }

    /**
     * LLD: Observer triggered after a user submits an answer (AnswerSubmittedEvent).
     */
    @EventListener
    @Transactional
    public void handleAnswerSubmission(AnswerSubmittedEvent event) {
        User user = userRepository.findById(event.getQuestionAuthorId()).orElse(null);
        if (user == null) return;

        // Example 2: Activity-based badge check (e.g., Answering questions)
        checkActivityBadges(user);
    }

    private void checkReputationBadges(User user) {
        // Condition: Reach 100 reputation
        if (user.getReputation() >= 100) {
            assignBadgeIfMissing(user, "Populist", BadgeRank.BRONZE);
        }
        // Condition: Reach 500 reputation
        if (user.getReputation() >= 500) {
            assignBadgeIfMissing(user, "Silver Populist", BadgeRank.SILVER);
        }
        // ... GOLD badge checks ...
    }

    private void checkActivityBadges(User user) {
        // Condition: Post 10 Questions (Requires counting questions by author)
        long questionCount = questionRepository.countByAuthor(user);
        if (questionCount >= 10) {
            assignBadgeIfMissing(user, "Question Enthusiast", BadgeRank.BRONZE);
        }
        // NOTE: The AnswerSubmittedEvent currently only tracks the QUESTION author,
        // not the ANSWER author. This structure needs refinement for accurate answer-based tracking.
    }

    private void assignBadgeIfMissing(User user, String name, BadgeRank rank) {
        // Resilience: Only assign the badge if the user doesn't already have it
        if (badgeRepository.findByUserUsernameAndNameAndRank(user.getUsername(), name, rank).isEmpty()) {
            Badge badge = new Badge();
            badge.setUser(user);
            badge.setName(name);
            badge.setRank(rank);
            badgeRepository.save(badge);
            System.out.println(String.format("Observer: User %s awarded %s (%s) badge!", user.getUsername(), name, rank));
        }
    }
}