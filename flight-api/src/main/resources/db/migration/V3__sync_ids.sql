SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('"astronauts"', 'id')),
              (SELECT (MAX(astronauts.id) + 1) FROM astronauts), FALSE);

SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('"spacecrafts"', 'id')),
              (SELECT (MAX(spacecrafts.id) + 1) FROM spacecrafts), FALSE);

SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('"expeditions"', 'id')),
              (SELECT (MAX(expeditions.id) + 1) FROM expeditions), FALSE);

SELECT SETVAL((SELECT PG_GET_SERIAL_SEQUENCE('"users"', 'id')),
              (SELECT (MAX(users.id) + 1) FROM users), FALSE);