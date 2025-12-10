package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.repository.QuestionRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final QuestionRepository questionRepository;

    public TagService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Cacheable: Caches the result in the 'tags' region.
     * Since the key is not specified, it uses a SimpleKey (i.e., method signature)
     * The cache lives forever unless explicitly evicted or TTL expires (configured in RedisConfig).
     */
    @Cacheable(value = "tags")
    @Transactional(readOnly = true)
    public List<String> getAllUniqueTags() {
        // Production Note: This requires a custom JPA query to fetch all distinct tags efficiently.
        // For simplicity, we simulate the logic:
        System.out.println("--- CACHE MISS: Fetching ALL tags from Database ---");
        List<String> rawTags = questionRepository.findAll().stream()
                .flatMap(q -> q.getTags().stream())
                .distinct()
                .collect(Collectors.toList());

        return rawTags;
    }
}