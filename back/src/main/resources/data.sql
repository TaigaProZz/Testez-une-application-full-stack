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