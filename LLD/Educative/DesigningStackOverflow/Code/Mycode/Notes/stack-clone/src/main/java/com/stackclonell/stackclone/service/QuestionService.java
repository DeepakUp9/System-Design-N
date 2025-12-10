package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.strategy.QuestionSortingStrategy;
import com.stackclonell.stackclone.core.strategy.QuestionSortingStrategyFactory;
import com.stackclonell.stackclone.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest; // For creating Pageable
import org.springframework.data.domain.Pageable; // For accepting pagination parameters
import org.springframework.data.domain.Sort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * LLD: The Context class for the Strategy Pattern.
 * This class uses the Strategy and delegates the core logic (sorting).
 */
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    // LLD: Inject the Factory to allow runtime strategy selection
    private final QuestionSortingStrategyFactory strategyFactory;

    private final MarkdownService markdownService; // New dependency

    public QuestionService(QuestionRepository questionRepository, QuestionSortingStrategyFactory strategyFactory, MarkdownService markdownService) {
        this.questionRepository = questionRepository;
        this.strategyFactory = strategyFactory;
        this.markdownService = markdownService;
    }

    /**
     * The core method exposed to the API layer.
     * @param sortParam The user's requested sorting method (e.g., "votes").
     * @return The list of questions, sorted according to the chosen strategy.
     */
    public List<Question> getAllQuestions(String sortParam) {
        // 1. Get all questions (Persistence)
        // Production Note: In a real system, we would use pagination and fetching only open posts here.
        List<Question> questions = questionRepository.findAll();

        // 2. LLD: Select the appropriate Strategy at runtime
        QuestionSortingStrategy activeStrategy = strategyFactory.getStrategy(sortParam);

        // 3. LLD: Execute the Strategy, passing the data to be processed
        return activeStrategy.sort(questions);
    }

    /**
     * The core method exposed to the API layer, now accepting Pageable parameters.
     * @param sortParam The user's requested sorting method.
     * @param page The page number (0-indexed).
     * @param size The number of items per page.
     * @return A Page<Question> object containing the data and metadata.
     */
    public Page<Question> getAllQuestions(String sortParam, int page, int size) {
        // 1. LLD Strategy Selection: Get the appropriate Strategy
        QuestionSortingStrategy activeStrategy = strategyFactory.getStrategy(sortParam);

        // 2. LLD Strategy Execution: Get the Sort criteria from the Strategy
        Sort sortCriteria = activeStrategy.getSortCriteria();

        // 3. Pagination/Scalability: Combine the page request (page, size) with the Sort criteria
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // 4. Persistence: Delegate to the repository (the DB executes the sort/pagination)
        return questionRepository.findAll(pageable);
    }

    public Question saveQuestion(Question question) {
        // Production: Validate tags, body, title size.
        return questionRepository.save(question);
    }

    /**
     * Core method to fetch and sort questions, now accepting search and filter criteria.
     * The Strategy Pattern (sorting) remains the final piece of the Pageable object.
     * @param sortParam Strategy key ("newest", "votes").
     * @param keyword Optional keyword to search in titles.
     * @param tag Optional tag to filter by.
     * @param page Page number.
     * @param size Page size.
     * @return Paginated and sorted results.
     */
    public Page<Question> searchAndSortQuestions(
            String sortParam, String keyword, String tag, int page, int size) {

        // 1. LLD Strategy Selection: Determine the Sort criteria
        QuestionSortingStrategy activeStrategy = strategyFactory.getStrategy(sortParam);
        Sort sortCriteria = activeStrategy.getSortCriteria();

        // 2. Pagination/Scalability: Combine the request with the Strategy's Sort
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // Core Feature: Render Markdown before retgetQuestionByIdurning to the API layer
//        pageable.getContent().forEach(q -> {
//            // We assume the model property q.body is the raw markdown
//            q.setBody(markdownService.renderMarkdown(q.getBody()));
//        });

        // 3. Persistence: Execute the correct query based on search/filter presence

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasTag = tag != null && !tag.trim().isEmpty();

        // Search Logic (Resilience/Edge Cases)
        if (hasKeyword && hasTag) {
            String lowerKeyword = keyword.toLowerCase();
            return questionRepository.searchByTitleAndTag(lowerKeyword, tag, pageable);
        } else if (hasKeyword) {
            String lowerKeyword = keyword.toLowerCase();
            return questionRepository.searchByTitleKeyword(lowerKeyword, pageable);
        } else if (hasTag) {
            return questionRepository.findByTagsContaining(tag, pageable);
        } else {
            // Default Case: Simple sorted and paginated list (Existing logic)
            return questionRepository.findAll(pageable);
        }
    }

    /**
     * Cacheable: Read data from the 'questionById' cache.
     * If not found, execute the method and cache the result.
     * @param questionId The ID used as the cache key.
     */
    @Cacheable(value = "questionById", key = "#questionId")
    @Transactional(readOnly = true)
    public Question getQuestionById(Long questionId) {
        // Assume this method exists to fetch a single question
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found."));
    }

    /**
     * CachePut: Updates the entity in the database and also updates the cache entry.
     * This is useful when updating the view count or editing the question.
     */
    @CachePut(value = "questionById", key = "#question.id")
    @Transactional
    public Question updateQuestion(Question question) {
        // ... (Update logic here, e.g., view count increment) ...
        return questionRepository.save(question);
    }

    /**
     * CacheEvict: Removes the cached entry for a deleted question.
     */
    @CacheEvict(value = "questionById", key = "#questionId")
    @Transactional
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    // NOTE: Caching large lists (like the main question list) is complex due to sorting/pagination
    // and is best done using custom Redis logic (covered in advanced caching) or a separate cache region.
    // Placeholder for other Question service methods (create, update, etc.)
}