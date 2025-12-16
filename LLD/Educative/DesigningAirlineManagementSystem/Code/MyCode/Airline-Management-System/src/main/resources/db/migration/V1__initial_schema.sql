-- 1. Flights Table (Core Flight Data)
CREATE TABLE flights (
    id BIGSERIAL PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    origin VARCHAR(10) NOT NULL,
    destination VARCHAR(10) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL, -- Managed by Observer Pattern
    gate VARCHAR(10),
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    base_price DECIMAL(12, 2) NOT NULL, -- Used by Strategy Pattern
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- 2. Reservations Table (Context for State Pattern)
CREATE TABLE reservations (
    id UUID PRIMARY KEY,
    pnr VARCHAR(10) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL, -- PENDING, CONFIRMED, etc.
    total_amount DECIMAL(12, 2) NOT NULL,
    user_id VARCHAR(50) NOT NULL, -- Reference to Auth Service
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0
);

-- 3. Flight Segments (Leaves for Composite Pattern)
-- Links many flight legs to a single reservation/itinerary
CREATE TABLE reservation_segments (
    id BIGSERIAL PRIMARY KEY,
    reservation_id UUID NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    flight_id BIGINT NOT NULL REFERENCES flights(id),
    sequence_order INTEGER NOT NULL, -- To maintain itinerary order
    seat_number VARCHAR(10),
    seat_class VARCHAR(20) NOT NULL -- ECONOMY, BUSINESS
);

-- 4. Audit Table (For Production Compliance)
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    entity_name VARCHAR(50) NOT NULL,
    entity_id VARCHAR(50) NOT NULL,
    action VARCHAR(20) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(50),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for High Performance (Scalability)
CREATE INDEX idx_flights_departure ON flights(departure_time);
CREATE INDEX idx_reservations_pnr ON reservations(pnr);
CREATE INDEX idx_segments_res_id ON reservation_segments(reservation_id);