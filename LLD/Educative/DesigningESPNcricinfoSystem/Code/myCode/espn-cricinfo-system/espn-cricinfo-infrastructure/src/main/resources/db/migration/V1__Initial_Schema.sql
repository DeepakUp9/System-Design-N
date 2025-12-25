-- V1__Initial_Schema.sql
-- Initial database schema for ESPN Cricinfo System

-- Create custom types for enums
DO $$ BEGIN
    CREATE TYPE match_format AS ENUM ('T20', 'ODI', 'TEST', 'T10', 'HUNDRED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE match_status AS ENUM ('NOT_STARTED', 'IN_PROGRESS', 'PAUSED', 'COMPLETED', 'ABANDONED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE tournament_format AS ENUM ('WORLD_CUP', 'CHAMPIONS_TROPHY', 'IPL', 'PSL', 'BBL', 'TEST_SERIES', 'ODI_SERIES', 'T20_SERIES', 'LEAGUE', 'KNOCKOUT', 'ROUND_ROBIN');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE tournament_status AS ENUM ('UPCOMING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE ball_type AS ENUM ('LEGAL_DELIVERY', 'NO_BALL', 'WIDE', 'BYE', 'LEG_BYE', 'OVERTHROW');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Create teams table
CREATE TABLE IF NOT EXISTS teams (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    name VARCHAR(100) NOT NULL UNIQUE,
    short_name VARCHAR(10) NOT NULL UNIQUE,
    country VARCHAR(50) NOT NULL,
    description TEXT,
    founded_year INTEGER,
    home_ground VARCHAR(255),
    logo_url VARCHAR(500),
    primary_color VARCHAR(7),
    secondary_color VARCHAR(7),

    total_matches_played INTEGER NOT NULL DEFAULT 0,
    total_matches_won INTEGER NOT NULL DEFAULT 0,
    total_matches_lost INTEGER NOT NULL DEFAULT 0,
    total_matches_drawn INTEGER NOT NULL DEFAULT 0
);

-- Create players table
CREATE TABLE IF NOT EXISTS players (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    name VARCHAR(100) NOT NULL,
    full_name VARCHAR(150),
    role VARCHAR(20) NOT NULL CHECK (role IN ('BATSMAN', 'BOWLER', 'ALL_ROUNDER', 'WICKET_KEEPER')),
    nationality VARCHAR(50),
    date_of_birth DATE,
    age INTEGER CHECK (age >= 15 AND age <= 50),
    jersey_number VARCHAR(10),
    profile_picture_url VARCHAR(500),
    biography TEXT,
    height_meters DECIMAL(3,2),
    weight_kg DECIMAL(5,2),
    batting_style VARCHAR(15) CHECK (batting_style IN ('RIGHT_HANDED', 'LEFT_HANDED')),
    bowling_style VARCHAR(20) CHECK (bowling_style IN ('RIGHT_ARM_FAST', 'RIGHT_ARM_MEDIUM', 'RIGHT_ARM_SPIN', 'LEFT_ARM_FAST', 'LEFT_ARM_MEDIUM', 'LEFT_ARM_SPIN', 'NONE')),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    retirement_date DATE,

    career_stats JSONB,
    stats_by_format JSONB DEFAULT '{}'::jsonb,

    team_id BIGINT REFERENCES teams(id)
);

-- Create venues table
CREATE TABLE IF NOT EXISTS venues (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    capacity INTEGER,
    pitch_type VARCHAR(50),
    ownership VARCHAR(100),
    year_opened INTEGER,
    description TEXT,
    image_url VARCHAR(500),

    total_matches_hosted INTEGER NOT NULL DEFAULT 0,
    avg_first_innings_score DECIMAL(5,2),
    avg_chase_success_rate DECIMAL(5,2),
    avg_runs_per_wicket DECIMAL(4,2),
    highest_score VARCHAR(20),
    lowest_score VARCHAR(20),

    grass_coverage_percentage DECIMAL(5,2),
    bounce_rating INTEGER CHECK (bounce_rating >= 0 AND bounce_rating <= 10),
    pace_rating INTEGER CHECK (pace_rating >= 0 AND pace_rating <= 10),
    spin_rating INTEGER CHECK (spin_rating >= 0 AND spin_rating <= 10),
    outfield_rating INTEGER CHECK (outfield_rating >= 0 AND outfield_rating <= 10),

    time_zone VARCHAR(50),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    altitude_meters DECIMAL(7,2),

    temperature_celsius DECIMAL(4,1),
    humidity_percentage DECIMAL(5,2),
    wind_speed_kmh DECIMAL(5,2)
);

-- Create tournaments table
CREATE TABLE IF NOT EXISTS tournaments (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    name VARCHAR(200) NOT NULL,
    short_name VARCHAR(20),
    description TEXT,
    format tournament_format NOT NULL,
    status tournament_status NOT NULL DEFAULT 'UPCOMING',
    start_date DATE NOT NULL,
    end_date DATE,
    host_country VARCHAR(100),
    host_cities VARCHAR(200),

    total_teams INTEGER,
    total_matches INTEGER,
    matches_completed INTEGER NOT NULL DEFAULT 0,

    organizing_body VARCHAR(100),
    sponsor VARCHAR(100),
    prize_money_million DECIMAL(10,2),

    winner_team_id BIGINT REFERENCES teams(id),
    runner_up_team_id BIGINT REFERENCES teams(id),
    player_of_tournament VARCHAR(100)
);

-- Create matches table
CREATE TABLE IF NOT EXISTS matches (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    match_name VARCHAR(200) NOT NULL,
    format match_format NOT NULL,
    status match_status NOT NULL DEFAULT 'NOT_STARTED',
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    venue_id BIGINT NOT NULL REFERENCES venues(id),

    team1_id BIGINT NOT NULL REFERENCES teams(id),
    team2_id BIGINT NOT NULL REFERENCES teams(id),
    winner_team_id BIGINT REFERENCES teams(id),
    tournament_id BIGINT REFERENCES tournaments(id),

    current_innings_number INTEGER DEFAULT 0,
    is_first_innings BOOLEAN DEFAULT FALSE,
    is_second_innings BOOLEAN DEFAULT FALSE,

    total_overs INTEGER DEFAULT 0,
    total_balls INTEGER DEFAULT 0,

    result_type VARCHAR(50),
    result_description TEXT,
    man_of_the_match_player_id BIGINT,

    weather_condition VARCHAR(50),
    temperature_celsius DECIMAL(4,1),
    humidity_percentage DECIMAL(5,2),
    wind_speed_kmh DECIMAL(5,2),

    umpire1_name VARCHAR(100),
    umpire2_name VARCHAR(100),
    third_umpire_name VARCHAR(100),
    match_referee_name VARCHAR(100),

    CONSTRAINT different_teams CHECK (team1_id != team2_id),
    CONSTRAINT valid_winner CHECK (winner_team_id IS NULL OR winner_team_id IN (team1_id, team2_id))
);

-- Create innings table
CREATE TABLE IF NOT EXISTS innings (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    innings_number INTEGER NOT NULL,
    match_id BIGINT NOT NULL REFERENCES matches(id),
    batting_team_id BIGINT NOT NULL REFERENCES teams(id),
    bowling_team_id BIGINT NOT NULL REFERENCES teams(id),

    total_runs INTEGER NOT NULL DEFAULT 0,
    total_wickets INTEGER NOT NULL DEFAULT 0,
    total_overs INTEGER NOT NULL DEFAULT 0,
    total_balls INTEGER NOT NULL DEFAULT 0,

    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    is_declared BOOLEAN NOT NULL DEFAULT FALSE,

    declared_overs DECIMAL(4,1),
    declared_balls INTEGER,

    target_runs INTEGER,
    target_overs DECIMAL(4,1),
    target_balls INTEGER,

    CONSTRAINT different_teams_innings CHECK (batting_team_id != bowling_team_id),
    CONSTRAINT unique_innings_per_match UNIQUE (match_id, innings_number)
);

-- Create balls table
CREATE TABLE IF NOT EXISTS balls (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),

    match_id BIGINT NOT NULL REFERENCES matches(id),
    innings_id BIGINT REFERENCES innings(id),
    over_number INTEGER NOT NULL CHECK (over_number >= 0),
    ball_number INTEGER NOT NULL CHECK (ball_number >= 1 AND ball_number <= 6),

    ball_type ball_type NOT NULL,
    runs_scored INTEGER NOT NULL DEFAULT 0 CHECK (runs_scored >= 0 AND runs_scored <= 6),
    is_wicket BOOLEAN NOT NULL DEFAULT FALSE,
    is_boundary BOOLEAN NOT NULL DEFAULT FALSE,
    is_six BOOLEAN NOT NULL DEFAULT FALSE,

    bowler_name VARCHAR(100) NOT NULL,
    batsman_name VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP NOT NULL,

    ball_speed_kmh DECIMAL(5,2),
    pitch_location VARCHAR(50),
    shot_played VARCHAR(50),

    umpire_decision VARCHAR(20),
    reviewed BOOLEAN NOT NULL DEFAULT FALSE,
    review_result VARCHAR(50),

    commentary TEXT,
    notes TEXT
);

-- Create junction tables for many-to-many relationships
CREATE TABLE IF NOT EXISTS tournament_teams (
    tournament_id BIGINT NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
    team_id BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    PRIMARY KEY (tournament_id, team_id)
);

CREATE TABLE IF NOT EXISTS tournament_venues (
    tournament_id BIGINT NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
    venue_id BIGINT NOT NULL REFERENCES venues(id) ON DELETE CASCADE,
    PRIMARY KEY (tournament_id, venue_id)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_teams_name ON teams(name);
CREATE INDEX IF NOT EXISTS idx_teams_country ON teams(country);
CREATE INDEX IF NOT EXISTS idx_teams_active ON teams(is_deleted);

CREATE INDEX IF NOT EXISTS idx_players_name ON players(name);
CREATE INDEX IF NOT EXISTS idx_players_team ON players(team_id);
CREATE INDEX IF NOT EXISTS idx_players_role ON players(role);
CREATE INDEX IF NOT EXISTS idx_players_active ON players(is_deleted);

CREATE INDEX IF NOT EXISTS idx_venues_city ON venues(city);
CREATE INDEX IF NOT EXISTS idx_venues_country ON venues(country);
CREATE INDEX IF NOT EXISTS idx_venues_active ON venues(is_deleted);

CREATE INDEX IF NOT EXISTS idx_tournaments_start_date ON tournaments(start_date);
CREATE INDEX IF NOT EXISTS idx_tournaments_status ON tournaments(status);
CREATE INDEX IF NOT EXISTS idx_tournaments_active ON tournaments(is_deleted);

CREATE INDEX IF NOT EXISTS idx_matches_start_time ON matches(start_time);
CREATE INDEX IF NOT EXISTS idx_matches_format ON matches(format);
CREATE INDEX IF NOT EXISTS idx_matches_status ON matches(status);
CREATE INDEX IF NOT EXISTS idx_matches_venue ON matches(venue_id);
CREATE INDEX IF NOT EXISTS idx_matches_winner ON matches(winner_team_id);
CREATE INDEX IF NOT EXISTS idx_matches_tournament ON matches(tournament_id);
CREATE INDEX IF NOT EXISTS idx_matches_active ON matches(is_deleted);

CREATE INDEX IF NOT EXISTS idx_innings_match ON innings(match_id);
CREATE INDEX IF NOT EXISTS idx_innings_number ON innings(innings_number);
CREATE INDEX IF NOT EXISTS idx_innings_batting_team ON innings(batting_team_id);
CREATE INDEX IF NOT EXISTS idx_innings_active ON innings(is_deleted);

CREATE INDEX IF NOT EXISTS idx_balls_match ON balls(match_id);
CREATE INDEX IF NOT EXISTS idx_balls_innings ON balls(innings_id);
CREATE INDEX IF NOT EXISTS idx_balls_over ON balls(over_number);
CREATE INDEX IF NOT EXISTS idx_balls_bowler ON balls(bowler_name);
CREATE INDEX IF NOT EXISTS idx_balls_batsman ON balls(batsman_name);
CREATE INDEX IF NOT EXISTS idx_balls_timestamp ON balls(timestamp);
CREATE INDEX IF NOT EXISTS idx_balls_active ON balls(is_deleted);

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers to all tables
DO $$ BEGIN
    CREATE TRIGGER update_teams_updated_at BEFORE UPDATE ON teams FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TRIGGER update_players_updated_at BEFORE UPDATE ON players FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TRIGGER update_venues_updated_at BEFORE UPDATE ON venues FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TRIGGER update_tournaments_updated_at BEFORE UPDATE ON tournaments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TRIGGER update_matches_updated_at BEFORE UPDATE ON matches FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TRIGGER update_innings_updated_at BEFORE UPDATE ON innings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TRIGGER update_balls_updated_at BEFORE UPDATE ON balls FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;
