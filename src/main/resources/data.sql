-- Roles
INSERT INTO roles(rolename) VALUES
                                ('ROLE_USER'),
                                ('ROLE_ADMIN');

-- Users with BCrypted password = password123
INSERT INTO users(email, password) VALUES
                                       ('admin@test.nl', '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('user@test.nl',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('nico@example.com',    '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('mirjam@example.com',  '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('jim@example.com',     '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('kristal@example.com', '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                       ('jop@example.com',     '$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW');

-- Link roles to users
INSERT INTO users_roles(roles_rolename, users_email) VALUES
                                                         ('ROLE_ADMIN', 'admin@test.nl'),
                                                         ('ROLE_USER',  'user@test.nl'),
                                                         ('ROLE_USER',  'nico@example.com'),
                                                         ('ROLE_USER',  'mirjam@example.com'),
                                                         ('ROLE_USER',  'jim@example.com'),
                                                         ('ROLE_USER',  'kristal@example.com'),
                                                         ('ROLE_USER',  'jop@example.com');

-- Students (linked to user via user_email foreign key)
INSERT INTO student(id, firstname, lastname, default_slot, active_member, user_email) VALUES
                                                                                          ('1', 'Nico',    'Heijnen',  'Vrijdag Avond', true, 'nico@example.com'),
                                                                                          ('2', 'Mirjam',  'Koopmans', 'Vrijdag Avond', true, 'mirjam@example.com'),
                                                                                          ('4', 'Jim',     'Bakmans',  'Vrijdag Avond', true, 'jim@example.com'),
                                                                                          ('5', 'Kristal', 'Kaars',    'Vrijdag Avond', true, 'kristal@example.com'),
                                                                                          ('6', 'Jop',     'Popper',   'Vrijdag Avond', true, 'jop@example.com');
