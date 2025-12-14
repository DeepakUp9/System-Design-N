----------------------------------------------------------------
-- V1__initial_schema.sql
-- Initial schema creation for the Jigsaw Game Service.
----------------------------------------------------------------

-- 1. Game Table (Context for the State Pattern)
CREATE TABLE jigsaw_game (
    game_id UUID PRIMARY KEY,

    -- State Pattern tracking
    current_state VARCHAR(50) NOT NULL, -- e.g., 'InProgressState'

    -- Game properties
    board_width INT NOT NULL,
    board_height INT NOT NULL,

    -- Audit/Meta data (CRITICAL for production)
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    version BIGINT NOT NULL -- For Optimistic Locking (Resilience/Concurrency)
);

-- 2. Piece Table (Context for the Strategy Pattern)
CREATE TABLE puzzle_piece (
    piece_id UUID PRIMARY KEY,
    game_id UUID NOT NULL, -- Foreign Key to the game

    -- Strategy Pattern Type
    piece_type VARCHAR(20) NOT NULL, -- e.g., 'CORNER', 'EDGE'

    -- Piece Structure
    flat_edge_count INT NOT NULL,

    -- Piece State
    rotation INT NOT NULL DEFAULT 0,
    current_row INT NULL,
    current_col INT NULL,

    FOREIGN KEY (game_id) REFERENCES jigsaw_game(game_id)
);

-- 3. Player Table (Simplified)
CREATE TABLE player (
    player_id UUID PRIMARY KEY,
    game_id UUID NOT NULL,
    username VARCHAR(100) NOT NULL,
    is_host BOOLEAN NOT NULL DEFAULT FALSE,
    score INT NOT NULL DEFAULT 0,

    FOREIGN KEY (game_id) REFERENCES jigsaw_game(game_id)
);

-- Indexing for performance
CREATE INDEX idx_piece_game_id ON puzzle_piece (game_id);
CREATE INDEX idx_player_game_id ON player (game_id);

-- Trigger for auto-updating 'updated_at' timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_update_game_timestamp
BEFORE UPDATE ON jigsaw_game
FOR EACH ROW EXECUTE PROCEDURE update_timestamp();