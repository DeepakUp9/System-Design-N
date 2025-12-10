-- Security: Ensure User table has unique and not null constraints
CREATE TABLE platform_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(60) NOT NULL,
    reputation BIGINT DEFAULT 1 NOT NULL,
    member_since TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Base Post table (Parent of Question and Answer)
CREATE TABLE post (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES platform_user(id), -- Index 1: Foreign Key
    body TEXT NOT NULL,
    score INTEGER DEFAULT 0 NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_edit_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_accepted BOOLEAN DEFAULT FALSE NOT NULL,
    status VARCHAR(20) NOT NULL -- Corresponds to PostStatus enum
);

-- Question table
CREATE TABLE question (
    id BIGINT PRIMARY KEY REFERENCES post(id),
    title VARCHAR(255) NOT NULL,
    view_count INTEGER DEFAULT 0 NOT NULL
);

-- Index 2: Index on question title for basic search/lookup (Performance)
CREATE INDEX idx_question_title ON question(title);

-- Separate table for tags (M:N relationship)
CREATE TABLE question_tags (
    question_id BIGINT NOT NULL REFERENCES question(id),
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (question_id, tag)
);

-- Answer table
CREATE TABLE answer (
    id BIGINT PRIMARY KEY REFERENCES post(id),
    parent_question_id BIGINT NOT NULL REFERENCES question(id) -- Index 3: Foreign Key
);

-- Index 4: Index on parent_question_id for fetching all answers (Performance)
CREATE INDEX idx_answer_question_id ON answer(parent_question_id);

