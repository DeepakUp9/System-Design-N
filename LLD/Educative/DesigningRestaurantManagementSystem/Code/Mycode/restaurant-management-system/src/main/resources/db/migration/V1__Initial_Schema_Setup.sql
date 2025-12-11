-- V1__Initial_Schema_Setup.sql - Flyway Migration Script

-- Create necessary sequences for IDs
CREATE SEQUENCE IF NOT EXISTS rms_user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS branch_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS orders_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS menu_item_seq START WITH 1 INCREMENT BY 1;

-- Table for multiple Restaurant Branches (Crucial for multi-branch requirement)
CREATE TABLE branch (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(512),
    is_online_enabled BOOLEAN DEFAULT TRUE,
    is_dine_in_enabled BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_branch PRIMARY KEY (id)
);

-- Table for Users (Will be tied into Spring Security)
CREATE TABLE rms_user (
    id BIGINT NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    role VARCHAR(50) NOT NULL, -- e.g., 'ADMIN', 'MANAGER', 'WAITER', 'KITCHEN'
    branch_id BIGINT,
    CONSTRAINT pk_rms_user PRIMARY KEY (id),
    CONSTRAINT fk_user_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

-- Table for Core Orders (Context for the State Pattern)
CREATE TABLE orders (
    id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    channel_type VARCHAR(50) NOT NULL, -- 'DINE_IN', 'ONLINE_DELIVERY', 'TAKEOUT' (Strategy type)
    order_status VARCHAR(50) NOT NULL, -- 'NEW', 'PROCESSING', 'PAID', etc. (State type)
    total_amount NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

-- Initial data to ensure system is operational on first run (optional but good practice)
INSERT INTO branch (id, name, address, is_online_enabled, is_dine_in_enabled)
VALUES (nextval('branch_seq'), 'Headquarters Downtown', '123 Main St', TRUE, TRUE);

INSERT INTO rms_user (id, username, password, email, role, branch_id)
VALUES (nextval('rms_user_seq'), 'admin', '{noop}password', 'admin@rms.com', 'ADMIN', 1);

-- Note: The password {noop}password is for initial testing/setup only.
-- We will replace this with BCRYPT in the next security step.