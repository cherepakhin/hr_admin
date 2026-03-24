CREATE TABLE positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Вставляем должность по умолчанию
INSERT INTO positions (name) VALUES ('Сотрудник');