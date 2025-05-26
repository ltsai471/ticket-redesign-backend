-- Create user table
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `age` INT,
    INDEX `idx_user_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create campaign table
CREATE TABLE IF NOT EXISTS `campaign` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    INDEX `idx_campaign_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create seat table
CREATE TABLE IF NOT EXISTS `seat` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `area` VARCHAR(50) NOT NULL,
    `row` INT NOT NULL,
    `column` INT NOT NULL,
    `price` INT NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `campaign_id` BIGINT NOT NULL,
    INDEX `idx_seat_campaign_area_row_column` (`campaign_id`, `area`, `row`, `column`),
    FOREIGN KEY (`campaign_id`) REFERENCES `campaign`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci; 

-- Create ticket table
CREATE TABLE IF NOT EXISTS `ticket` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `userId` BIGINT NOT NULL,
    `seatId` BIGINT NOT NULL,
    `paid` BOOLEAN DEFAULT FALSE,
    `creationDate` DATE,
    FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
    FOREIGN KEY (`seatId`) REFERENCES `seat`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;