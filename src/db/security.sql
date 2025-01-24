CREATE TABLE IF NOT EXISTS authorities (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           authority VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(100) NOT NULL,
                                     enabled BOOLEAN DEFAULT TRUE,
                                     authority_id INT NOT NULL,
                                     CONSTRAINT FK_authority_id FOREIGN KEY (authority_id) REFERENCES authorities(id)
);
