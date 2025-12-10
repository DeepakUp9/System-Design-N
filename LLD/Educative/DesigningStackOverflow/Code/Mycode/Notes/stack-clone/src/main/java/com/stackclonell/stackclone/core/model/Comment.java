package com.stackclonell.stackclone.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text; // The content of the comment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // LLD: Attach the comment to the generic Post entity (covers Question and Answer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post parentPost;

    private Integer score = 0; // Simple voting on comments

    private LocalDateTime createdDate = LocalDateTime.now();
}