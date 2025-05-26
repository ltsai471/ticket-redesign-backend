CREATE TABLE IF NOT EXISTS campaign (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS seat (
    id VARCHAR(36) PRIMARY KEY,
    campaign_id VARCHAR(36) NOT NULL,
    area VARCHAR(50) NOT NULL,
    row INT NOT NULL,
    column INT NOT NULL,
    price INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (campaign_id) REFERENCES campaign(id),
    UNIQUE KEY unique_seat (campaign_id, area, row, column)
);

CREATE TABLE IF NOT EXISTS ticket (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    seat_id VARCHAR(36) NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    creation_date DATETIME NOT NULL,
    FOREIGN KEY (seat_id) REFERENCES seat(id)
); 