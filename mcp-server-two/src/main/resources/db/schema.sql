-- Create mcp_books table
CREATE TABLE IF NOT EXISTS mcp_books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publish_date DATE,
    isbn VARCHAR(20)
); 