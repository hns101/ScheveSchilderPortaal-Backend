-- === ROLES ===
INSERT INTO roles(rolename) VALUES
                                ('ROLE_USER'),
                                ('ROLE_ADMIN');

-- === USERS (BCrypted password: password123) ===
INSERT INTO users(email, password) VALUES
                                       ('admin@test.nl',     '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('user@test.nl',      '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('nico@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('john@example.com','$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('jim@example.com',   '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('kristal@example.com','$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('jop@example.com',   '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('eva@example.com',   '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('mark@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('lisa@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('tom@example.com',   '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('anna@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('hns101@live.nl',   '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('sophie@example.com','$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('ruben@example.com', '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('naomi@example.com', '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('daan@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('lars@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('emma@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('fleur@example.com', '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW');

-- === USER ROLES ===
INSERT INTO users_roles(roles_rolename, users_email) VALUES
                                                         ('ROLE_ADMIN', 'admin@test.nl'),
                                                         ('ROLE_USER',  'user@test.nl'),
                                                         ('ROLE_USER',  'nico@example.com'),
                                                         ('ROLE_USER',  'john@example.com'),
                                                         ('ROLE_USER',  'jim@example.com'),
                                                         ('ROLE_USER',  'kristal@example.com'),
                                                         ('ROLE_USER',  'jop@example.com'),
                                                         ('ROLE_USER',  'eva@example.com'),
                                                         ('ROLE_USER',  'mark@example.com'),
                                                         ('ROLE_USER',  'lisa@example.com'),
                                                         ('ROLE_USER',  'tom@example.com'),
                                                         ('ROLE_USER',  'anna@example.com'),
                                                         ('ROLE_USER',  'hns101@live.nl'),
                                                         ('ROLE_USER',  'sophie@example.com'),
                                                         ('ROLE_USER',  'ruben@example.com'),
                                                         ('ROLE_USER',  'naomi@example.com'),
                                                         ('ROLE_USER',  'daan@example.com'),
                                                         ('ROLE_USER',  'lars@example.com'),
                                                         ('ROLE_USER',  'emma@example.com'),
                                                         ('ROLE_USER',  'fleur@example.com');

-- === STUDENTS ===
INSERT INTO student(firstname, lastname, default_slot, is_active, user_email) VALUES
                                                                                  ('Nico',    'Klaasen',  'Vrijdag Avond',    true, 'nico@example.com'),
                                                                                  ('John',  'Doe', 'Woensdag Avond',   true, 'john@example.com'),
                                                                                  ('Eva',     'Janssen',  'Zaterdag Ochtend', true, 'eva@example.com'),
                                                                                  ('Jim',     'Bakmans',  'Vrijdag Avond',    true, 'jim@example.com'),
                                                                                  ('Kristal', 'Kaars',    'Woensdag Avond',   true, 'kristal@example.com'),
                                                                                  ('Jop',     'Popper',   'Zaterdag Ochtend', true, 'jop@example.com'),
                                                                                  ('Mark',    'De Vries', 'Vrijdag Avond',    true, 'mark@example.com'),
                                                                                  ('Lisa',    'Bakker',   'Woensdag Avond',   true, 'lisa@example.com'),
                                                                                  ('Tom',     'Mulder',   'Woensdag Avond',   true, 'tom@example.com'),
                                                                                  ('Anna',    'Vermeer',  'Zaterdag Ochtend', true, 'anna@example.com'),
                                                                                  ('Hans',     'Bregman',  'Vrijdag Avond',    true, 'hns101@live.nl'),
                                                                                  ('Sophie',  'Meijer',   'Woensdag Avond',   true, 'sophie@example.com'),
                                                                                  ('Ruben',   'Dekker',   'Woensdag Avond',   true, 'ruben@example.com'),
                                                                                  ('Naomi',   'Vos',      'Vrijdag Avond',    true, 'naomi@example.com'),
                                                                                  ('Daan',    'Kuiper',   'Woensdag Avond',   true, 'daan@example.com'),
                                                                                  ('Lars',    'Koning',   'Zaterdag Ochtend', true, 'lars@example.com'),
                                                                                  ('Emma',    'Blom',     'Woensdag Avond',   true, 'emma@example.com'),
                                                                                  ('Fleur',   'Peeters',  'Woensdag Avond',   true, 'fleur@example.com');

-- === WEEKS ===
INSERT INTO week(week_num, start_date) VALUES
                                           (1, '2025-02-03'),
                                           (2, '2025-02-10'),
                                           (3, '2025-02-17');

-- === LESSONS ===
INSERT INTO lesson(slot, time, date, week_id) VALUES
                                                  ('Woensdag Avond', '19:00 - 21:30', '2025-02-05', 1),
                                                  ('Vrijdag Avond',  '19:00 - 21:30', '2025-02-07', 1),
                                                  ('Zaterdag Ochtend', '09:00 - 11:30', '2025-02-08', 1),
                                                  ('Woensdag Avond', '19:00 - 21:30', '2025-02-12', 2),
                                                  ('Vrijdag Avond',  '19:00 - 21:30', '2025-02-14', 2),
                                                  ('Zaterdag Ochtend', '09:00 - 11:30', '2025-02-15', 2),
                                                  ('Woensdag Avond', '19:00 - 21:30', '2025-02-19', 3),
                                                  ('Vrijdag Avond',  '19:00 - 21:30', '2025-02-21', 3),
                                                  ('Zaterdag Ochtend', '09:00 - 11:30', '2025-02-22', 3);

-- === LESSON-STUDENT LINKS ===
INSERT INTO lesson_students(lesson_id, student_id) VALUES
                                                       (1, 2), (1, 5), (1, 8), (1, 9), (1, 12), (1, 13), (1, 15), (1, 17), (1, 18),
                                                       (2, 1), (2, 4), (2, 7), (2, 11), (2, 14),
                                                       (3, 3), (3, 6), (3, 10), (3, 16),
                                                       (4, 2), (4, 5), (4, 8), (4, 9), (4, 12), (4, 13), (4, 15), (4, 17), (4, 18),
                                                       (5, 1), (5, 4), (5, 7), (5, 11), (5, 14),
                                                       (6, 3), (6, 6), (6, 10), (6, 16),
                                                       (7, 2), (7, 5), (7, 8), (7, 9), (7, 12), (7, 13), (7, 15), (7, 17), (7, 18),
                                                       (8, 1), (8, 4), (8, 7), (8, 11), (8, 14),
                                                       (9, 3), (9, 6), (9, 10), (9, 16);

-- === GALLERIES ===
-- Updated to include the is_public column, defaulting to true
INSERT INTO gallery(student_id, is_public) VALUES
                                               (1, true), (2, true), (3, true), (4, true), (5, true),
                                               (6, true), (7, true), (8, true), (9, true), (10, true);

-- === ARTWORKS ===
INSERT INTO artwork(title, year, photo_url, gallery_id, student_id) VALUES
                                                                        ('Zonsondergang', '2023', 'Zonsondergang.png', 2, 2),
                                                                        ('Zelfportret', '2024', 'Zelfportret.png', 2, 2),
                                                                        ('Bloemenveld', '2022', 'Bloemenveld.png', 2, 2),
                                                                        ('Abstract Licht', '2024', 'Abstract Licht.png', 2, 2),
                                                                        ('Berglandschap', '2021', 'Berglandschap.png', 2, 2),
                                                                        ('Droomwereld', '2023', 'Droomwereld.png', 2, 2),
                                                                        ('Lentegevoel', '2024', 'Lentegevoel.png', 2, 2),
                                                                        ('Familie', '2023', 'Familie.png', 2, 2);