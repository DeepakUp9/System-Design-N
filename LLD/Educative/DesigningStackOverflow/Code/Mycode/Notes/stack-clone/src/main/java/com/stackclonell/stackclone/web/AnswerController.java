package com.stackclonell.stackclone.web;

import com.stackclonell.stackclone.core.model.Answer;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.service.AnswerService;
import com.stackclonell.stackclone.service.PostService;
import com.stackclonell.stackclone.repository.QuestionRepository;
import com.stackclonell.stackclone.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final PostService postService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public AnswerController(AnswerService answerService, PostService postService, QuestionRepository questionRepository, UserRepository userRepository) {
        this.answerService = answerService;
        this.postService = postService;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint to submit a new answer to a specific question.
     */
    @PostMapping
    public ResponseEntity<Answer> submitAnswer(@PathVariable Long questionId,
                                               @RequestBody Answer answer,
                                               Authentication authentication) {

        Question parentQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

        answer.setParentQuestion(parentQuestion);
        answer.setAuthor(currentUser);
        answer.setCreationDate(LocalDateTime.now());

        Answer createdAnswer = answerService.saveAnswer(answer);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }

    /**
     * Endpoint to handle voting on an Answer.
     * LLD: Triggers the State and Observer Patterns in the PostService.
     */
    @PostMapping("/{answerId}/vote")
    public ResponseEntity<Void> voteAnswer(@PathVariable Long answerId,
                                           @RequestParam int voteValue) {

        if (voteValue != 1 && voteValue != -1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // LLD: Delegate the logic to the PostService (unified voting logic)
        postService.voteOnPost(answerId, voteValue);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * [GET] Retrieves all answers for a question, with Markdown rendered.
     */
    @GetMapping
    public ResponseEntity<List<Answer>> getAnswers(@PathVariable Long questionId) {

        // We call the service method which applies the Markdown rendering
        List<Answer> answers = answerService.getAnswersForQuestion(questionId);

        return ResponseEntity.ok(answers);
    }

    // Production Note: You would also need endpoints for getAnswers, acceptAnswer, etc.
}