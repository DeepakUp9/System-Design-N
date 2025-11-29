-- V1__init.sql

CREATE TABLE branch (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  street VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(100),
  postal_code VARCHAR(20),
  country VARCHAR(100),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE parking_stall (
  id UUID PRIMARY KEY,
  branch_id UUID NOT NULL REFERENCES branch(id),
  stall_code VARCHAR(50),
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE vehicle (
  id UUID PRIMARY KEY,
  vehicle_type VARCHAR(50) NOT NULL,
  subtype VARCHAR(50),
  license_plate VARCHAR(50) UNIQUE,
  vin VARCHAR(100) UNIQUE,
  make VARCHAR(100),
  model VARCHAR(100),
  year INTEGER,
  mileage BIGINT,
  status VARCHAR(50) NOT NULL,
  parking_stall_id UUID,
  branch_id UUID,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT fk_branch FOREIGN KEY (branch_id) REFERENCES branch(id),
  CONSTRAINT fk_stall FOREIGN KEY (parking_stall_id) REFERENCES parking_stall(id)
);

CREATE TABLE account (
  id UUID PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255),
  account_type VARCHAR(50) NOT NULL,
  full_name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  phone VARCHAR(50),
  status VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE vehicle_log (
  id UUID PRIMARY KEY,
  vehicle_id UUID NOT NULL REFERENCES vehicle(id),
  log_type VARCHAR(50),
  description TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE reservation (
  id UUID PRIMARY KEY,
  vehicle_id UUID NOT NULL REFERENCES vehicle(id),
  account_id UUID NOT NULL REFERENCES account(id),
  pickup_branch_id UUID,
  dropoff_branch_id UUID,
  start_time TIMESTAMP WITH TIME ZONE,
  end_time TIMESTAMP WITH TIME ZONE,
  status VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE equipment (
  id UUID PRIMARY KEY,
  name VARCHAR(100),
  description TEXT,
  price NUMERIC(10,2)
);

CREATE TABLE service (
  id UUID PRIMARY KEY,
  name VARCHAR(100),
  description TEXT,
  price NUMERIC(10,2)
);

CREATE TABLE reservation_equipment (
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  equipment_id UUID NOT NULL REFERENCES equipment(id),
  PRIMARY KEY (reservation_id, equipment_id)
);

CREATE TABLE reservation_service (
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  service_id UUID NOT NULL REFERENCES service(id),
  PRIMARY KEY (reservation_id, service_id)
);

CREATE TABLE payment (
  id UUID PRIMARY KEY,
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  amount NUMERIC(12,2),
  currency VARCHAR(10),
  method VARCHAR(50),
  status VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE fine (
  id UUID PRIMARY KEY,
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  amount NUMERIC(12,2),
  reason TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
