package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Core query for fetching comments on a specific post, ordered by date
    // Scalability: Uses Pagination
    Page<Comment> findByParentPostIdOrderByCreatedDateAsc(Long parentPostId, Pageable pageable);
}