CREATE DATABASE nutechtestapi;

CREATE TABLE users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    profile_image VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE balances (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT,
    saldo DECIMAL(12, 2),
    CONSTRAINT fk_user_balance FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE services (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    service_code VARCHAR(20) NOT NULL UNIQUE,
    service_name VARCHAR(100) NOT NULL,
    description TEXT,
    tarif DECIMAL(12, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO services (service_code, service_name, description, tarif, created_at)
VALUES 
    ('PAJAK', 'Pajak PBB', 'dummy', 40000, CURRENT_TIMESTAMP),
    ('PLN', 'Listrik', 'dummy', 10000, CURRENT_TIMESTAMP),
    ('PDAM', 'PDAM Berlangganan', 'dummy', 40000, CURRENT_TIMESTAMP),
    ('PULSA', 'Pulsa', 'dummy', 40000, CURRENT_TIMESTAMP),
    ('PGN', 'PGN Berlangganan', 'dummy', 50000, CURRENT_TIMESTAMP),
    ('MUSIK', 'Musik Berlangganan', 'dummy', 50000, CURRENT_TIMESTAMP),
    ('TV', 'TV Berlangganan', 'dummy', 50000, CURRENT_TIMESTAMP),
    ('PAKET_DATA', 'Paket data', 'dummy', 50000, CURRENT_TIMESTAMP),
    ('VOUCHER_GAME', 'Voucher Game', 'dummy', 100000, CURRENT_TIMESTAMP),
    ('VOUCHER_MAKANAN', 'Voucher Makanan', 'dummy', 100000, CURRENT_TIMESTAMP),
    ('QURBAN', 'Qurban', 'dummy', 200000, CURRENT_TIMESTAMP),
    ('ZAKAT', 'Zakat', 'dummy', 300000, CURRENT_TIMESTAMP);

CREATE TABLE transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    invoice_number VARCHAR(50),
    service_id BIGINT,
    amount DECIMAL(12, 2),
    transaction_type VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_transaction FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_service FOREIGN KEY (service_id) REFERENCES services(id)
);

CREATE SEQUENCE invoice_seq START 1 INCREMENT 1;

