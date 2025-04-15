DROP TABLE IF EXISTS `PARTICIPATE`;
DROP TABLE IF EXISTS `SESSIONS`;
DROP TABLE IF EXISTS `TEACHERS`;
DROP TABLE IF EXISTS `USERS`;


CREATE TABLE `TEACHERS` (
                            `id` INT PRIMARY KEY AUTO_INCREMENT,
                            `last_name` VARCHAR(40),
                            `first_name` VARCHAR(40),
                            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `SESSIONS` (
                            `id` INT PRIMARY KEY AUTO_INCREMENT,
                            `name` VARCHAR(50),
                            `description` VARCHAR(2000),
                            `date` TIMESTAMP,
                            `teacher_id` int,
                            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `USERS` (
                         `id` INT PRIMARY KEY AUTO_INCREMENT,
                         `last_name` VARCHAR(40),
                         `first_name` VARCHAR(40),
                         `admin` BOOLEAN NOT NULL DEFAULT false,
                         `email` VARCHAR(255),
                         `password` VARCHAR(255),
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `PARTICIPATE` (
                               `user_id` INT,
                               `session_id` INT
);

ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('teacherFirstName1', 'teacherLastName1'),
       ('teacherFirstName2', 'teacherLastName2');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES  ('admin', 'admin', true, 'admin@admin.com', '$2a$10$kVCvXM3t0jCgjZJMJ8Px1ukizxo7NdxlgOEyJTv.D8La/KB9dCY1y'),
        ('a', 'a', false, 'a@a.a', '$2a$10$kVCvXM3t0jCgjZJMJ8Px1ukizxo7NdxlgOEyJTv.D8La/KB9dCY1y'),
        ('b', 'b', false, 'b@b.b', '$2a$10$kVCvXM3t0jCgjZJMJ8Px1ukizxo7NdxlgOEyJTv.D8La/KB9dCY1y');

INSERT INTO SESSIONS (name, description, teacher_id, date)
VALUES  ('Session 1', 'Session 1 description', 1, '2025-01-01 20:00:00'),
        ('Session 2', 'Session 2 description', 1, '2025-01-01 20:00:00'),
        ('Session 3', 'Session 3 description', 2, '2025-01-01 20:00:00'),
        ('Session 4', 'Session 4 description', 2, '2025-01-01 20:00:00');