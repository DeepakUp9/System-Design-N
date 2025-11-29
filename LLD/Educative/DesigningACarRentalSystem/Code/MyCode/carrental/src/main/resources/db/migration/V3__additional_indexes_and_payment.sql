-- V3__additional_indexes_and_payment.sql

-- Additional indexes
CREATE INDEX idx_reservation_times ON reservation(start_time, end_time);
CREATE INDEX idx_reservation_vehicle ON reservation(vehicle_id);

-- Payment enhancements
ALTER TABLE payment ADD COLUMN idempotency_key VARCHAR(255);
ALTER TABLE payment ADD COLUMN transaction_id VARCHAR(255);
CREATE INDEX idx_payment_status ON payment(status);
CREATE INDEX idx_payment_idempotency ON payment(idempotency_key);

