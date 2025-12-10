package com.stackclonell.stackclone.core.event;

import com.stackclonell.stackclone.core.model.Answer;
import com.stackclonell.stackclone.core.model.Question;
import lombok.Getter;

@Getter
public class AnswerSubmittedEvent {
    private final Long questionId;
    private final Long questionAuthorId;
    private final Long answerId;
    private final String questionTitle;
    private final String answerAuthorUsername;

    public AnswerSubmittedEvent(Question question, Answer answer) {
        this.questionId = question.getId();
        this.questionAuthorId = question.getAuthor().getId();
        this.answerId = answer.getId();
        this.questionTitle = question.getTitle();
        this.answerAuthorUsername = answer.getAuthor().getUsername();
    }
}