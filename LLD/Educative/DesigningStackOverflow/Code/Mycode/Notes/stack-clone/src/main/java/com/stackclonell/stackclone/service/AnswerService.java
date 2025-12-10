package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.event.AnswerSubmittedEvent;
import com.stackclonell.stackclone.core.model.Answer;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.repository.AnswerRepository;
import com.stackclonell.stackclone.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MarkdownService markdownService; // Inject this
    private final WebSocketService webSocketService; // New dependency

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, ApplicationEventPublisher eventPublisher, MarkdownService markdownService, WebSocketService webSocketService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.eventPublisher = eventPublisher; // Inject the publisher
        this.markdownService = markdownService;
        this.webSocketService = webSocketService;
    }

    @Transactional
    public Answer saveAnswer(Answer answer) {
        Answer createdAnswer = answerRepository.save(answer);

        // Retrieve the parent question to get the author details for the notification event
        Question parentQuestion = questionRepository.findById(answer.getParentQuestion().getId())
                .orElseThrow(() -> new RuntimeException("Parent question not found."));

        // LLD: Publish the new Answer Event (Observer Pattern)
        eventPublisher.publishEvent(new AnswerSubmittedEvent(parentQuestion, createdAnswer));

        // 2. Real-Time: Notify clients via WebSocket (UX)
        webSocketService.notifyNewAnswer(parentQuestion.getId(), Map.of(
                "type", "newAnswer",
                "answerId", createdAnswer.getId(),
                "authorUsername", createdAnswer.getAuthor().getUsername()
        ));

        return createdAnswer;
    }
}