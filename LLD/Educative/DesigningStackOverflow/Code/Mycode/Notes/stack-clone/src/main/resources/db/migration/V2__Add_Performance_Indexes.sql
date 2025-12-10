-- Performance: Indexes to support Strategy Pattern (Sorting)
-- Sorting by 'newest'
CREATE INDEX idx_post_creation_date ON post(creation_date DESC);
-- Sorting by 'votes'
CREATE INDEX idx_post_score ON post(score DESC);

-- Performance: Index for high-traffic reputation lookups
CREATE INDEX idx_user_reputation ON platform_user(reputation);