-- Add performance indexes and optimizations
-- V4__Add_Performance_Indexes.sql

-- Match table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_status_format ON match(status, format);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_start_time ON match(start_time);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_end_time ON match(end_time);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_venue ON match(venue_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_tournament ON match(tournament_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_team1 ON match(team1_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_team2 ON match(team2_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_winner ON match(winner_id);

-- Innings table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_innings_match ON innings(match_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_innings_team ON innings(team_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_innings_status ON innings(status);

-- Ball table indexes (performance critical)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ball_match_innings ON ball(match_id, innings_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ball_batsman ON ball(batsman_name);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ball_bowler ON ball(bowler_name);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ball_over_number ON ball(over_number);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ball_timestamp ON ball(timestamp);

-- Player table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_team ON player(team_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_name ON player(name);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_role ON player(role);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_nationality ON player(nationality);

-- Team table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_team_name ON team(name);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_team_country ON team(country);

-- Tournament table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_tournament_status ON tournament(status);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_tournament_start_date ON tournament(start_date);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_tournament_end_date ON tournament(end_date);

-- Venue table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_venue_city ON venue(city);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_venue_country ON venue(country);

-- Partial indexes for active/completed entities
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_active ON match(start_time)
WHERE status IN ('NOT_STARTED', 'IN_PROGRESS', 'PAUSED');

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_completed ON match(end_time)
WHERE status = 'COMPLETED';

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_innings_active ON innings(created_at)
WHERE status IN ('NOT_STARTED', 'IN_PROGRESS');

-- Composite indexes for common queries
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_team_date ON match(team1_id, team2_id, start_time);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ball_match_over ON ball(match_id, over_number);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_stats ON player(team_id, role);

-- JSONB indexes for flexible data
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_stats_gin ON player(career_stats) WHERE career_stats IS NOT NULL;
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_team_stats_gin ON team(team_stats) WHERE team_stats IS NOT NULL;

-- Full-text search indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_match_name_fts ON match USING gin(to_tsvector('english', match_name));
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_player_name_fts ON player USING gin(to_tsvector('english', name));
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_team_name_fts ON team USING gin(to_tsvector('english', name));

-- Create materialized view for match statistics
CREATE MATERIALIZED VIEW match_statistics_mv AS
SELECT
    m.id as match_id,
    m.match_name,
    m.format,
    m.status,
    m.start_time,
    m.end_time,
    t1.name as team1_name,
    t2.name as team2_name,
    COALESCE(winner.name, 'N/A') as winner_name,
    COALESCE(SUM(i.total_runs), 0) as total_runs,
    COALESCE(SUM(i.total_wickets), 0) as total_wickets,
    COUNT(DISTINCT i.id) as total_innings,
    COUNT(b.id) as total_balls,
    AVG(i.run_rate) as avg_run_rate
FROM match m
LEFT JOIN team t1 ON m.team1_id = t1.id
LEFT JOIN team t2 ON m.team2_id = t2.id
LEFT JOIN team winner ON m.winner_id = winner.id
LEFT JOIN innings i ON m.id = i.match_id
LEFT JOIN ball b ON i.id = b.innings_id
GROUP BY m.id, m.match_name, m.format, m.status, m.start_time, m.end_time,
         t1.name, t2.name, winner.name;

-- Create unique index on materialized view
CREATE UNIQUE INDEX idx_match_stats_mv_id ON match_statistics_mv(match_id);

-- Create function to refresh materialized view
CREATE OR REPLACE FUNCTION refresh_match_statistics()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY match_statistics_mv;
END;
$$ LANGUAGE plpgsql;

-- Create view for player performance
CREATE OR REPLACE VIEW player_performance_v AS
SELECT
    p.id,
    p.name,
    p.role,
    t.name as team_name,
    COUNT(DISTINCT m.id) as matches_played,
    COALESCE(SUM(b.runs_scored), 0) as total_runs,
    COALESCE(SUM(CASE WHEN b.is_wicket = true THEN 1 ELSE 0 END), 0) as wickets_taken,
    ROUND(
        CASE
            WHEN COUNT(b.id) > 0 THEN (SUM(b.runs_scored)::decimal / COUNT(b.id)) * 100
            ELSE 0
        END, 2
    ) as batting_strike_rate,
    ROUND(
        CASE
            WHEN SUM(CASE WHEN b.ball_type = 'LEGAL_DELIVERY' THEN 1 ELSE 0 END) > 0
            THEN SUM(b.total_runs)::decimal / SUM(CASE WHEN b.ball_type = 'LEGAL_DELIVERY' THEN 1 ELSE 0 END) * 6
            ELSE 0
        END, 2
    ) as bowling_economy
FROM player p
LEFT JOIN team t ON p.team_id = t.id
LEFT JOIN ball b ON (b.batsman_name = p.name OR b.bowler_name = p.name)
LEFT JOIN innings i ON b.innings_id = i.id
LEFT JOIN match m ON i.match_id = m.id
GROUP BY p.id, p.name, p.role, t.name;

-- Create indexes on views (for materialized view)
CREATE INDEX idx_player_perf_runs ON player_performance_v(total_runs DESC);
CREATE INDEX idx_player_perf_wickets ON player_performance_v(wickets_taken DESC);
