-- V2__Sample_Data.sql
-- Sample data for development and testing

-- Insert sample teams
INSERT INTO teams (name, short_name, country, description, founded_year, home_ground, primary_color, secondary_color) VALUES
('India', 'IND', 'India', 'Indian National Cricket Team', 1932, 'Various', '#0033A0', '#FF9933'),
('Australia', 'AUS', 'Australia', 'Australian National Cricket Team', 1877, 'Various', '#FFD700', '#00843D'),
('England', 'ENG', 'England', 'England National Cricket Team', 1877, 'Lord''s Cricket Ground', '#FFFFFF', '#C8102E'),
('South Africa', 'SA', 'South Africa', 'South African National Cricket Team', 1889, 'Various', '#007A4D', '#FFFFFF'),
('New Zealand', 'NZ', 'New Zealand', 'New Zealand National Cricket Team', 1894, 'Various', '#000000', '#FFFFFF'),
('Pakistan', 'PAK', 'Pakistan', 'Pakistan National Cricket Team', 1952, 'Various', '#01411C', '#FFFFFF'),
('West Indies', 'WI', 'West Indies', 'West Indies National Cricket Team', 1926, 'Various', '#7B0041', '#FFD700'),
('Sri Lanka', 'SL', 'Sri Lanka', 'Sri Lankan National Cricket Team', 1981, 'R. Premadasa Stadium', '#000080', '#FFD700'),
('Bangladesh', 'BAN', 'Bangladesh', 'Bangladeshi National Cricket Team', 1977, 'Sher-e-Bangla Stadium', '#F42A41', '#006A4E'),
('Afghanistan', 'AFG', 'Afghanistan', 'Afghan National Cricket Team', 1995, 'Various', '#000000', '#D4AF37');

-- Insert sample venues
INSERT INTO venues (name, city, country, capacity, pitch_type, year_opened, avg_first_innings_score, avg_runs_per_wicket, bounce_rating, pace_rating, spin_rating, outfield_rating, latitude, longitude) VALUES
('Melbourne Cricket Ground', 'Melbourne', 'Australia', 100024, 'GRASS', 1853, 280.5, 32.5, 8, 7, 6, 9, -37.819967, 144.983449),
('Lord''s Cricket Ground', 'London', 'England', 30000, 'GRASS', 1814, 265.8, 31.2, 7, 6, 8, 8, 51.529972, -0.174899),
('Eden Gardens', 'Kolkata', 'India', 68000, 'GRASS', 1864, 295.3, 35.7, 6, 5, 9, 7, 22.564506, 88.342728),
('Wankhede Stadium', 'Mumbai', 'India', 33000, 'TURF', 1974, 275.2, 33.1, 7, 6, 7, 8, 18.938771, 72.825665),
('MCG', 'Melbourne', 'Australia', 100024, 'DROPS', 1853, 285.6, 34.2, 8, 8, 5, 9, -37.819967, 144.983449),
('Old Trafford', 'Manchester', 'England', 19000, 'GRASS', 1857, 255.9, 29.8, 6, 7, 6, 7, 53.463056, -2.291389),
('Sydney Cricket Ground', 'Sydney', 'Australia', 48602, 'GRASS', 1848, 270.4, 31.9, 7, 6, 7, 8, -33.891475, 151.224683),
('The Oval', 'London', 'England', 23580, 'GRASS', 1845, 260.7, 30.5, 6, 5, 8, 7, 51.483702, -0.114867),
('WACA Ground', 'Perth', 'Australia', 24000, 'GRASS', 1893, 245.3, 28.4, 9, 9, 4, 8, -31.959167, 115.873333),
('Newlands', 'Cape Town', 'South Africa', 25000, 'GRASS', 1888, 250.8, 29.2, 7, 8, 5, 7, -33.969444, 18.472222);

-- Insert sample players
INSERT INTO players (name, full_name, role, nationality, age, batting_style, bowling_style, team_id) VALUES
('Virat Kohli', 'Virat Kohli', 'BATSMAN', 'India', 35, 'RIGHT_HANDED', 'NONE', (SELECT id FROM teams WHERE short_name = 'IND')),
('Steve Smith', 'Steven Peter Devereux Smith', 'BATSMAN', 'Australia', 34, 'RIGHT_HANDED', 'NONE', (SELECT id FROM teams WHERE short_name = 'AUS')),
('Joe Root', 'Joseph Edward Root', 'BATSMAN', 'England', 33, 'RIGHT_HANDED', 'RIGHT_ARM_MEDIUM', (SELECT id FROM teams WHERE short_name = 'ENG')),
('Kane Williamson', 'Kane Stuart Williamson', 'BATSMAN', 'New Zealand', 33, 'RIGHT_HANDED', 'RIGHT_ARM_MEDIUM', (SELECT id FROM teams WHERE short_name = 'NZ')),
('Jasprit Bumrah', 'Jasprit Jasbirsingh Bumrah', 'BOWLER', 'India', 30, 'RIGHT_HANDED', 'RIGHT_ARM_FAST', (SELECT id FROM teams WHERE short_name = 'IND')),
('Pat Cummins', 'Patrick James Cummins', 'BOWLER', 'Australia', 30, 'RIGHT_HANDED', 'RIGHT_ARM_FAST', (SELECT id FROM teams WHERE short_name = 'AUS')),
('James Anderson', 'James Michael Anderson', 'BOWLER', 'England', 41, 'LEFT_HANDED', 'LEFT_ARM_FAST', (SELECT id FROM teams WHERE short_name = 'ENG')),
('Trent Boult', 'Trent Alexander Boult', 'BOWLER', 'New Zealand', 34, 'RIGHT_HANDED', 'LEFT_ARM_FAST', (SELECT id FROM teams WHERE short_name = 'NZ')),
('MS Dhoni', 'Mahendra Singh Dhoni', 'WICKET_KEEPER', 'India', 42, 'RIGHT_HANDED', 'NONE', (SELECT id FROM teams WHERE short_name = 'IND')),
('Tim Paine', 'Timothy David Paine', 'WICKET_KEEPER', 'Australia', 39, 'RIGHT_HANDED', 'NONE', (SELECT id FROM teams WHERE short_name = 'AUS'));

-- Update team captains and wicket keepers
UPDATE teams SET captain_id = (SELECT id FROM players WHERE name = 'Virat Kohli') WHERE short_name = 'IND';
UPDATE teams SET wicket_keeper_id = (SELECT id FROM players WHERE name = 'MS Dhoni') WHERE short_name = 'IND';

UPDATE teams SET captain_id = (SELECT id FROM players WHERE name = 'Steve Smith') WHERE short_name = 'AUS';
UPDATE teams SET wicket_keeper_id = (SELECT id FROM players WHERE name = 'Tim Paine') WHERE short_name = 'AUS';

UPDATE teams SET captain_id = (SELECT id FROM players WHERE name = 'Joe Root') WHERE short_name = 'ENG';

UPDATE teams SET captain_id = (SELECT id FROM players WHERE name = 'Kane Williamson') WHERE short_name = 'NZ';

-- Insert sample tournament
INSERT INTO tournaments (name, short_name, format, status, start_date, end_date, host_country, total_teams, total_matches, organizing_body, sponsor, prize_money_million) VALUES
('ICC Cricket World Cup 2023', 'CWC2023', 'WORLD_CUP', 'COMPLETED', '2023-10-05', '2023-11-19', 'India', 10, 48, 'ICC', 'Various', 10.0);

-- Insert tournament teams
INSERT INTO tournament_teams (tournament_id, team_id)
SELECT t.id, team.id
FROM tournaments t
CROSS JOIN teams team
WHERE t.name = 'ICC Cricket World Cup 2023'
AND team.short_name IN ('IND', 'AUS', 'ENG', 'SA', 'NZ', 'PAK', 'WI', 'SL', 'BAN', 'AFG');

-- Insert tournament venues
INSERT INTO tournament_venues (tournament_id, venue_id)
SELECT t.id, v.id
FROM tournaments t
CROSS JOIN venues v
WHERE t.name = 'ICC Cricket World Cup 2023'
AND v.city IN ('Ahmedabad', 'Delhi', 'Mumbai', 'Kolkata', 'Chennai', 'Bangalore', 'Hyderabad', 'Jaipur', 'Dharamsala', 'Lucknow');

-- Insert sample match
INSERT INTO matches (match_name, format, status, start_time, end_time, venue_id, team1_id, team2_id, winner_team_id, tournament_id, result_type, umpire1_name, umpire2_name) VALUES
('India vs Australia', 'ODI', 'COMPLETED', '2023-11-15 14:00:00', '2023-11-15 18:30:00',
 (SELECT id FROM venues WHERE name = 'Wankhede Stadium'),
 (SELECT id FROM teams WHERE short_name = 'IND'),
 (SELECT id FROM teams WHERE short_name = 'AUS'),
 (SELECT id FROM teams WHERE short_name = 'IND'),
 (SELECT id FROM tournaments WHERE name = 'ICC Cricket World Cup 2023'),
 'NORMAL',
 'Nigel Llong', 'Richard Kettleborough');

-- Update team statistics
UPDATE teams SET
    total_matches_played = total_matches_played + 1,
    total_matches_won = CASE WHEN short_name = 'IND' THEN total_matches_won + 1 ELSE total_matches_won END,
    total_matches_lost = CASE WHEN short_name = 'AUS' THEN total_matches_lost + 1 ELSE total_matches_lost END
WHERE short_name IN ('IND', 'AUS');

-- Insert sample innings
INSERT INTO innings (innings_number, match_id, batting_team_id, bowling_team_id, total_runs, total_wickets, total_overs, total_balls, is_completed) VALUES
(1, (SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM teams WHERE short_name = 'IND'),
 (SELECT id FROM teams WHERE short_name = 'AUS'),
 285, 7, 50, 300, true);

INSERT INTO innings (innings_number, match_id, batting_team_id, bowling_team_id, total_runs, total_wickets, total_overs, total_balls, is_completed, target_runs, target_overs) VALUES
(2, (SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM teams WHERE short_name = 'AUS'),
 (SELECT id FROM teams WHERE short_name = 'IND'),
 245, 10, 47, 282, true, 286, 50);

-- Insert sample balls (simplified - just a few for demonstration)
INSERT INTO balls (match_id, innings_id, over_number, ball_number, ball_type, runs_scored, is_boundary, bowler_name, batsman_name, timestamp) VALUES
((SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM innings WHERE innings_number = 1),
 1, 1, 'LEGAL_DELIVERY', 4, true, 'Pat Cummins', 'Rohit Sharma', '2023-11-15 14:00:00'),
((SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM innings WHERE innings_number = 1),
 1, 2, 'LEGAL_DELIVERY', 1, false, 'Pat Cummins', 'Rohit Sharma', '2023-11-15 14:00:30'),
((SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM innings WHERE innings_number = 1),
 1, 3, 'LEGAL_DELIVERY', 0, false, 'Pat Cummins', 'Rohit Sharma', '2023-11-15 14:01:00'),
((SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM innings WHERE innings_number = 1),
 1, 4, 'LEGAL_DELIVERY', 6, true, 'Pat Cummins', 'Rohit Sharma', '2023-11-15 14:01:30'),
((SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM innings WHERE innings_number = 1),
 1, 5, 'LEGAL_DELIVERY', 1, false, 'Pat Cummins', 'Rohit Sharma', '2023-11-15 14:02:00'),
((SELECT id FROM matches WHERE match_name = 'India vs Australia'),
 (SELECT id FROM innings WHERE innings_number = 1),
 1, 6, 'LEGAL_DELIVERY', 2, false, 'Pat Cummins', 'Rohit Sharma', '2023-11-15 14:02:30');

-- Update match statistics
UPDATE matches SET
    current_innings_number = 2,
    is_first_innings = true,
    is_second_innings = true,
    total_overs = 97,
    total_balls = 582,
    result_description = 'India won by 40 runs'
WHERE match_name = 'India vs Australia';

-- Update tournament progress
UPDATE tournaments SET matches_completed = 1 WHERE name = 'ICC Cricket World Cup 2023';
