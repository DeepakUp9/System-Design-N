package com.stackclonell.stackclone.core.strategy;

import com.stackclonell.stackclone.core.model.Question;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * LLD: The Strategy Interface. Defines the contract for all ranking algorithms.
 */
public interface QuestionSortingStrategy {

    /**
     * Executes the specific sorting logic.
     * @param questions The list of questions to be sorted.
     * @return The sorted list of questions.
     */
    List<Question> sort(List<Question> questions);

    /**
     * Returns a unique identifier for this strategy.
     * Used by the Strategy Factory for mapping/lookup.
     */
    String getStrategyName();


    /**
     * Returns the Spring Data Sort object for the specific ranking algorithm.
     */
    Sort getSortCriteria();


}