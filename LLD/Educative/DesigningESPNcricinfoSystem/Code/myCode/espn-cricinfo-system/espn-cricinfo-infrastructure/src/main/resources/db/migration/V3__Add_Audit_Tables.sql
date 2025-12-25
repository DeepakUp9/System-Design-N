-- Add audit tables for tracking changes
-- V3__Add_Audit_Tables.sql

-- Create audit table for tracking all entity changes
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    record_id BIGINT NOT NULL,
    operation VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    old_values JSONB,
    new_values JSONB,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    user_agent TEXT
);

-- Create audit table specifically for matches
CREATE TABLE match_audit (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (match_id) REFERENCES match(id) ON DELETE CASCADE
);

-- Create index on audit tables for performance
CREATE INDEX idx_audit_log_table_record ON audit_log(table_name, record_id);
CREATE INDEX idx_audit_log_changed_at ON audit_log(changed_at);
CREATE INDEX idx_match_audit_match_id ON match_audit(match_id);
CREATE INDEX idx_match_audit_changed_at ON match_audit(changed_at);

-- Create table for system configuration
CREATE TABLE system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(200) UNIQUE NOT NULL,
    config_value TEXT,
    config_type VARCHAR(50) NOT NULL, -- STRING, INTEGER, BOOLEAN, JSON
    description TEXT,
    is_editable BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert default system configurations
INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
('match.max_concurrent_matches', '10', 'INTEGER', 'Maximum number of concurrent live matches'),
('scoring.auto_validate', 'true', 'BOOLEAN', 'Enable automatic ball validation'),
('cache.match_ttl_seconds', '3600', 'INTEGER', 'Cache TTL for match data in seconds'),
('notification.live_updates', 'true', 'BOOLEAN', 'Enable live score notifications'),
('security.max_login_attempts', '5', 'INTEGER', 'Maximum login attempts before lockout'),
('kafka.retry_attempts', '3', 'INTEGER', 'Number of Kafka message retry attempts');

-- Create table for user sessions
CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_token VARCHAR(500) UNIQUE NOT NULL,
    ip_address INET,
    user_agent TEXT,
    device_info JSONB,
    login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry_time TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for user sessions
CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_session_token ON user_sessions(session_token);
CREATE INDEX idx_user_sessions_expiry ON user_sessions(expiry_time);
CREATE INDEX idx_user_sessions_active ON user_sessions(is_active);

-- Create table for API rate limiting
CREATE TABLE rate_limits (
    id BIGSERIAL PRIMARY KEY,
    client_id VARCHAR(200) NOT NULL, -- API key or user identifier
    endpoint VARCHAR(500) NOT NULL,
    request_count INTEGER NOT NULL DEFAULT 0,
    window_start TIMESTAMP NOT NULL,
    window_end TIMESTAMP NOT NULL,
    limit_exceeded BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(client_id, endpoint, window_start)
);

-- Create indexes for rate limiting
CREATE INDEX idx_rate_limits_client_endpoint ON rate_limits(client_id, endpoint);
CREATE INDEX idx_rate_limits_window ON rate_limits(window_start, window_end);
