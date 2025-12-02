-- Best practice: Use a dedicated schema for the banking application (e.g., 'atm_system')
-- CREATE SCHEMA IF NOT EXISTS atm_system;
-- SET search_path TO atm_system, public;

-- =========================================================================================
-- 1. CUSTOMER Table
-- =========================================================================================
-- Stores the basic demographic information of the bank customer.
CREATE TABLE customer (
    customer_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT a,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(15) UNIQUE,
    address TEXT,
    date_created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =========================================================================================
-- 2. ACCOUNT Table
-- =========================================================================================
-- Stores the financial accounts linked to a customer.
-- The balance MUST be a NUMERIC type to prevent floating-point arithmetic errors.
CREATE TYPE account_type AS ENUM ('SAVINGS', 'CHECKING');

CREATE TABLE account (
    account_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id),
    account_number VARCHAR(20) UNIQUE NOT NULL, -- The number displayed to the customer
    account_type account_type NOT NULL,

    -- Crucial: NUMERIC(19, 4) for high precision, storing up to 4 decimal places
    -- (for currencies like JPY that don't use cents, the 4 places provide buffer)
    balance NUMERIC(19, 4) DEFAULT 0.00 NOT NULL CHECK (balance >= 0),

    is_active BOOLEAN DEFAULT TRUE,
    date_created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Index for fast lookup by customer
    CREATE INDEX idx_account_customer ON account (customer_id);
);

-- =========================================================================================
-- 3. CARD Table
-- =========================================================================================
-- Stores the ATM/Debit card details.
-- PIN is stored as a hash (e.g., bcrypt hash) for security.
CREATE TYPE card_status AS ENUM ('ACTIVE', 'BLOCKED', 'EXPIRED');

CREATE TABLE card (
    card_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customer(customer_id),
    account_id BIGINT NOT NULL REFERENCES account(account_id),

    card_number VARCHAR(16) UNIQUE NOT NULL, -- 16 digits

    -- Best practice: Store PIN as a HASH, never plaintext
    pin_hash VARCHAR(100) NOT NULL,

    expiry_date DATE NOT NULL,
    cvv_hash VARCHAR(100), -- Stored as hash for internal use/compliance (not needed for ATM txns)

    status card_status DEFAULT 'ACTIVE' NOT NULL,

    -- Critical edge case: Track failed attempts to implement a PIN lockout mechanism
    pin_fail_count INT DEFAULT 0 NOT NULL,
    max_pin_fail_count INT DEFAULT 3 NOT NULL,

    date_created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Index for fast lookup by card number (critical for transaction initiation)
    CREATE UNIQUE INDEX idx_card_number ON card (card_number);
);

-- =========================================================================================
-- 4. TRANSACTION Table
-- =========================================================================================
-- The immutable ledger of all ATM activity.
CREATE TYPE transaction_type AS ENUM ('WITHDRAWAL', 'DEPOSIT', 'TRANSFER', 'BALANCE_INQUIRY');
CREATE TYPE transaction_status AS ENUM ('COMPLETED', 'PENDING', 'FAILED');

CREATE TABLE transaction (
    transaction_id BIGSERIAL PRIMARY KEY,

    -- Link to the account being debited/credited. (Can be null for transfers from other banks)
    account_id BIGINT REFERENCES account(account_id),
    card_id BIGINT REFERENCES card(card_id),

    transaction_type transaction_type NOT NULL,
    transaction_status transaction_status DEFAULT 'PENDING' NOT NULL,

    -- Amount: Can be 0 for BALANCE_INQUIRY
    amount NUMERIC(19, 4) DEFAULT 0.00 NOT NULL CHECK (amount >= 0),

    -- Reference details for transfers and deposits (e.g., recipient account number)
    reference_account VARCHAR(20),

    -- Edge Case: Store the ATM identifier for auditing and fraud analysis
    atm_identifier VARCHAR(50),

    transaction_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index for fast retrieval of transaction history
CREATE INDEX idx_transaction_account ON transaction (account_id, transaction_timestamp DESC);