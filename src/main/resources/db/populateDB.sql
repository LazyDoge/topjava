DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description) VALUES
  (100000, '2018-07-9 10:22:30', 'Прием1'),
  (100000, '2018-07-8 17:22:30', 'Прием2'),
  (100000, '2018-07-7 14:22:30', 'Прием3'),
  (100000, '2018-07-9 3:22:30', 'Прием4'),
  (100001, '2018-07-8 1:22:30', 'Прием5'),
  (100001, '2018-07-7 2:22:30', 'Прием6');

