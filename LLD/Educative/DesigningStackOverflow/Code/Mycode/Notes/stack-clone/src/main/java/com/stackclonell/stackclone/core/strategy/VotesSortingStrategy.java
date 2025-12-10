package com.stackclonell.stackclone.core.strategy;

import com.stackclonell.stackclone.core.model.Question;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Sort;
/**
 * LLD: Concrete Strategy 2. Sorts by the current score (total votes).
 */
@Component
public class VotesSortingStrategy implements QuestionSortingStrategy {

    @Override
    public List<Question> sort(List<Question> questions) {
        // Sorts by score in descending order (Highest score first)
        questions.sort(Comparator.comparing(Question::getScore).reversed());
        System.out.println("Strategy: Applied 'Votes' sorting.");
        return questions;
    }

    @Override
    public String getStrategyName() {
        return "votes";
    }


    @Override
    public Sort getSortCriteria() {
        // SCALABILITY: Sorts using the 'score' column, leveraging the DB index (V2 Migration)
        return Sort.by(Sort.Direction.DESC, "score");
    }


}