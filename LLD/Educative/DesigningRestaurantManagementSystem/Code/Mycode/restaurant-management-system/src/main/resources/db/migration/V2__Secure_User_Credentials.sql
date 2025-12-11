-- V2__Secure_User_Credentials.sql - Secures the admin password using BCrypt

-- Note: This hash corresponds to the plaintext 'password'
UPDATE rms_user SET password = '$2a$10$wK1Vqj9k/bZzE.lS2uO4m.S/G2xO3gT5yL0c6J8vQ7Xw/A6yV5lQ'
WHERE username = 'admin';