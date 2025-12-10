package com.stackclonell.stackclone.core.strategy;

import com.stackclonell.stackclone.core.model.Question;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 * LLD: Concrete Strategy 1. Sorts by the latest creation date.
 */
@Component
public class NewestSortingStrategy implements QuestionSortingStrategy {

    @Override
    public List<Question> sort(List<Question> questions) {
        // Sorts by creationDate in descending order (Newest first)
        questions.sort(Comparator.comparing(Question::getCreationDate).reversed());
        System.out.println("Strategy: Applied 'Newest' sorting.");
        return questions;
    }

    @Override
    public String getStrategyName() {
        return "newest";
    }

    @Override
    public Sort getSortCriteria() {
        // SCALABILITY: Sorts using the 'creationDate' column, leveraging the DB index (V2 Migration)
        return Sort.by(Sort.Direction.DESC, "creationDate");
    }

}