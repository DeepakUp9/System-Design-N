package com.stackclonell.stackclone.core.event;

import com.stackclonell.stackclone.core.model.Post;
import lombok.Getter;
import java.time.Clock;

/**
 * LLD: The Concrete Event (Subject/Observable Payload).
 * Stores immutable data about the event that occurred.
 */
@Getter
public class PostVotedEvent {

    private final Long postId;
    private final Long authorId;
    private final int voteValue;
    private final boolean isQuestion;

    public PostVotedEvent(Post source, int voteValue) {
        this.postId = source.getId();
        this.authorId = source.getAuthor().getId();
        this.voteValue = voteValue;
        this.isQuestion = source instanceof com.stackclonell.stackclone.core.model.Question;
    }

    // For logging and traceability (Scalability/Resilience)
    public long getTimestamp() {
        return Clock.systemUTC().millis();
    }
}