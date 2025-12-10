package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence layer for the abstract Post entity (Parent of Question and Answer).
 * Used by PostService to manage votes and state transitions, regardless of post type.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Basic CRUD operations inherited
}