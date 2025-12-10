package com.stackclonell.stackclone.core.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Question extends Post {

    private String title;

    // Simple implementation for tags - can be optimized later
    @ElementCollection
    private java.util.Set<String> tags;

    private Integer viewCount = 0;

    // LLD: Implementation of the delegated State method (sends event for Observer Pattern)
    @Override
    public void vote(int voteValue) {
        // Core logic: Delegate to the current state object
        if (this.currentState != null) {
            this.currentState.handleVote(this, voteValue);
        }
        // In reality, this would also trigger an Observer/Event
    }
}