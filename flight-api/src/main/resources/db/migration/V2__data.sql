-- FILL TABLES WITH DEV DATA --

truncate table astronauts restart identity cascade;

insert into astronauts (id, name, is_busy)
values (1, 'Neil', false);
insert into astronauts (id, name, is_busy)
values (2, 'Buzz', false);
insert into astronauts (id, name, is_busy)
values (3, 'Michael', false);

insert into spacecrafts (id, model, enabled)
values (1, 'Apollo 11', true);

insert into authorities (id, value)
values (0, 'ROLE_RECRUITER');
insert into authorities (id, value)
values (1, 'ROLE_OPERATOR');