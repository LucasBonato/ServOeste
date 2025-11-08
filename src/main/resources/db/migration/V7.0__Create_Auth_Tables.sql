CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    role enum('ADMIN', 'EMPLOYEE', 'TECHNICIAN') DEFAULT 'EMPLOYEE'
);

CREATE TABLE refresh_token (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    revoked_at DATETIME(6) DEFAULT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);