package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.model.Answer;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.AnswerRepository;
import com.stackclonell.stackclone.repository.BadgeRepository;
import com.stackclonell.stackclone.repository.QuestionRepository;
import com.stackclonell.stackclone.repository.UserRepository;
import com.stackclonell.stackclone.web.dto.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final BadgeRepository badgeRepository;

    public UserService(UserRepository userRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, BadgeRepository badgeRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.badgeRepository = badgeRepository;
    }

    /**
     * Aggregates user details and activity history into a comprehensive DTO.
     */
    @Transactional(readOnly = true) // Performance: Read-only transaction
    public UserProfileDto getUserProfile(Long userId, Pageable questionPageable, Pageable answerPageable) {

        // 1. Fetch core user details
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));


        // 2. Fetch earned badges
        List<Badge> badges = badgeRepository.findByUser(user);


        // 2. Fetch user's questions (Paginated)
        Page<Question> questions = questionRepository.findByAuthor(user, questionPageable);

        // 3. Fetch user's answers (Paginated)
        Page<Answer> answers = answerRepository.findByAuthor(user, answerPageable);

        // 4. Map to DTOs for the final structure
        Page<UserProfileDto.QuestionSummary> questionSummaries = questions.map(q -> UserProfileDto.QuestionSummary.builder()
                .id(q.getId())
                .title(q.getTitle())
                .score(q.getScore())
                .createdDate(q.getCreationDate())
                .build());

        Page<UserProfileDto.AnswerSummary> answerSummaries = answers.map(a -> UserProfileDto.AnswerSummary.builder()
                .id(a.getId())
                .parentQuestionId(a.getParentQuestion().getId())
                .parentQuestionTitle(a.getParentQuestion().getTitle())
                .score(a.getScore())
                .createdDate(a.getCreationDate())
                .build());

        // 5. Build the final UserProfileDto
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .reputation(user.getReputation())
                .memberSince(user.getMemberSince())
                .questions(questionSummaries)
                .answers(answerSummaries)
                .build();
    }
}