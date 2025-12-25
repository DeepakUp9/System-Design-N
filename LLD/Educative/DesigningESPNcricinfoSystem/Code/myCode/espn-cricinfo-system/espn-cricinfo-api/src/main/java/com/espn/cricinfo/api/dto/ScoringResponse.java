package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;

import java.time.LocalDateTime;

/**
 * DTO for scoring operation responses.
 */
public record ScoringResponse(
        boolean success,
        String message,
        ScoringResult scoringResult,
        Long matchId,
        Integer overNumber,
        Integer ballNumber,
        LocalDateTime timestamp,
        String commentary
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private ScoringResult scoringResult;
        private Long matchId;
        private Integer overNumber;
        private Integer ballNumber;
        private LocalDateTime timestamp;
        private String commentary;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder scoringResult(ScoringResult scoringResult) {
            this.scoringResult = scoringResult;
            return this;
        }

        public Builder matchId(Long matchId) {
            this.matchId = matchId;
            return this;
        }

        public Builder overNumber(Integer overNumber) {
            this.overNumber = overNumber;
            return this;
        }

        public Builder ballNumber(Integer ballNumber) {
            this.ballNumber = ballNumber;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder commentary(String commentary) {
            this.commentary = commentary;
            return this;
        }

        public ScoringResponse build() {
            return new ScoringResponse(success, message, scoringResult, matchId,
                    overNumber, ballNumber, timestamp, commentary);
        }
    }
}
