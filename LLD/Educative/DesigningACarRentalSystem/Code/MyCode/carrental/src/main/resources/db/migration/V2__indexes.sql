CREATE INDEX idx_reservation_vehicle_times ON reservation(vehicle_id, start_time, end_time);
CREATE INDEX idx_vehicle_branch_type_status ON vehicle(branch_id, vehicle_type, status);
CREATE INDEX idx_reservation_status ON reservation(status);
