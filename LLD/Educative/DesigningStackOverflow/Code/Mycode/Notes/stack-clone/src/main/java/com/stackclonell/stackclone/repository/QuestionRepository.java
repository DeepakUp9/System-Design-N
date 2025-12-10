package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.Question;
import com.stackclonell.stackclone.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page; // IMPORTANT: Import Page and Pageable
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Persistence layer for Question entities.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Custom query method to find questions by tag (essential for Stack Overflow functionality)
    List<Question> findByTagsContaining(String tag);

    // We can use findAll() which returns List<Question> for the Strategy Pattern's input.



    // Now uses Pageable to return a Page<Question> (a subset of data)
    // The Strategy Pattern will still receive List<Question>, so we define a custom method

    // Custom query to fetch a page of questions for the service layer
    Page<Question> findAll(Pageable pageable);

    // New: Method for counting all questions by a user (used by BadgeService)
    long countByAuthor(User author);



    // CRITICAL: We need a way to get ALL questions (without pagination) for the Strategy Pattern's
    // SORTING implementation, as Strategy sorts the result set, not the DB query.
    // However, for high scalability, the Strategy should be executed on the DB.
    // We will simulate the data flow while acknowledging the real-world limitation:

    // LLD Compromise for Strategy Pattern: To allow our in-memory Strategy sorting,
    // we must fetch the data first, but this is NOT scalable.
    // SCALABLE LLD FIX: The sorting should be implemented using Spring's Sort object
    // passed to findAll(Pageable), which leverages DB indexing (V2 Migration).
    // Let's refactor the Strategy Pattern to be scalable.


    // 1. Tag Filtering (uses the element collection mapping)
    // We still need a method to support tag searching with pagination
    Page<Question> findByTagsContaining(String tag, Pageable pageable);

    // 2. Keyword Search (using a custom JPQL query for LIKE comparison)
    // NOTE: This uses the index on the 'title' column only for prefix matching.
    @Query("SELECT q FROM Question q WHERE LOWER(q.title) LIKE %?1%")
    Page<Question> searchByTitleKeyword(String keyword, Pageable pageable);

    // 3. Combined Search/Filter (Used if both keyword and tag are present)
    @Query("SELECT q FROM Question q JOIN q.tags t WHERE LOWER(q.title) LIKE %?1% AND t = ?2")
    Page<Question> searchByTitleAndTag(String keyword, String tag, Pageable pageable);

    // New: Find all questions by author, paginated (Scalability)
    Page<Question> findByAuthor(User author, Pageable pageable);

}