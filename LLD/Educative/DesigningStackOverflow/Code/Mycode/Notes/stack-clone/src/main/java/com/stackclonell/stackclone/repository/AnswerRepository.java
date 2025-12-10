package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.Answer;
import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Persistence layer for Answer entities.
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Essential query: find all answers belonging to a specific question
    List<Answer> findByParentQuestion(Question parentQuestion);

    // New: Find all answers by author, paginated (Scalability)
    Page<Answer> findByAuthor(User author, Pageable pageable);
}