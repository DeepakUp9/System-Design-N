package com.stackclonell.stackclone.core.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Answer extends Post {

    @ManyToOne(fetch = FetchType.LAZY)
    private Question parentQuestion;

    // LLD: Implementation of the delegated State method
    @Override
    public void vote(int voteValue) {
        // Core logic: Delegate to the current state object
        if (this.currentState != null) {
            this.currentState.handleVote(this, voteValue);
        }
    }
}