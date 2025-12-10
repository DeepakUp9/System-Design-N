package com.stackclonell.stackclone.core.model;

import com.stackclonell.stackclone.core.state.PostState;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Lob // Large Object - for the body text
    private String body;

    private Integer score = 0; // Total votes
    private LocalDateTime creationDate = LocalDateTime.now();
    private LocalDateTime lastEditDate = LocalDateTime.now();
    private boolean isAccepted = false; // Only for Answers

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.OPEN;

    // LLD: Transient field to hold the current State object for the State Pattern
    @Transient
    protected PostState currentState;

    // LLD: Method to delegate behavior to the current State object
    public abstract void vote(int voteValue);

    // LLD: Method to set the new State (used by Concrete States)
    public void setCurrentState(PostState newState) {
        this.currentState = newState;
    }
}