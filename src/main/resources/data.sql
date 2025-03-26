insert into roles(rolename) values ('ROLE_USER'), ('ROLE_ADMIN');

insert into users(email,password) values ( 'admin@test.nl','$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW'),
                                         ( 'user@test.nl','$2a$10$N74RktuepMLWkReIrJf/L.vNNm.ILXq9DMMlpzn889lga1yNaPNBW' );

insert into users_roles(roles_rolename,users_email) values ('ROLE_ADMIN','admin@test.nl'),
                                                           ('ROLE_USER','user@test.nl');