package com.stackclonell.stackclone.core.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionSortingStrategyTest {

    @Test
    void newestStrategyShouldSortByCreationDateDescending() {
        QuestionSortingStrategy strategy = new NewestSortingStrategy();
        Sort sort = strategy.getSortCriteria();

        // 1. Check if the name is correct (for Factory lookup)
        assertEquals("newest", strategy.getStrategyName());

        // 2. Check if the generated Sort object is correct
        Sort.Order order = sort.getOrderFor("creationDate");
        assertTrue(order.isDescending());
        assertEquals("creationDate", order.getProperty());
    }

    @Test
    void votesStrategyShouldSortByScoreDescending() {
        QuestionSortingStrategy strategy = new VotesSortingStrategy();
        Sort sort = strategy.getSortCriteria();

        assertEquals("votes", strategy.getStrategyName());

        // 2. Check if the generated Sort object is correct
        Sort.Order order = sort.getOrderFor("score");
        assertTrue(order.isDescending());
        assertEquals("score", order.getProperty());
    }
}