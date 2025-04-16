CREATE TABLE IF NOT EXISTS resource (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    format VARCHAR(50) NOT NULL,
    creation_date DATE NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    courses_id INT NOT NULL,
    FOREIGN KEY (courses_id) REFERENCES course(id) ON DELETE CASCADE
); 