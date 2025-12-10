package com.stackclonell.stackclone.web.dto;

import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.Answer;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileDto {

    // Core User Information
    private Long id;
    private String username;
    private Long reputation;
    private LocalDateTime memberSince;

    // Activity Summary (Paginated for Scalability)
    private Page<QuestionSummary> questions;
    private Page<AnswerSummary> answers;

    @Data
    @Builder
    public static class QuestionSummary {
        private Long id;
        private String title;
        private int score;
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    public static class AnswerSummary {
        private Long id;
        private Long parentQuestionId;
        private String parentQuestionTitle;
        private int score;
        private LocalDateTime createdDate;
    }
}