-- Docker: mounted into /docker-entrypoint-initdb.d/ by docker-compose.yml

CREATE DATABASE IF NOT EXISTS bmi_localization
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bmi_localization;

CREATE TABLE IF NOT EXISTS bmi_results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    weight DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    bmi DOUBLE NOT NULL,
    language VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE USER IF NOT EXISTS 'bmi_app'@'localhost' IDENTIFIED BY 'Test12';
CREATE USER IF NOT EXISTS 'bmi_app'@'127.0.0.1' IDENTIFIED BY 'Test12';
GRANT ALL PRIVILEGES ON bmi_localization.* TO 'bmi_app'@'localhost';
GRANT ALL PRIVILEGES ON bmi_localization.* TO 'bmi_app'@'127.0.0.1';
FLUSH PRIVILEGES;
