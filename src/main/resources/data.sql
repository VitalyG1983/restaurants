INSERT INTO USERS (NAME, EMAIL, PASSWORD, CALORIES_PER_DAY)
VALUES ('User', 'user@yandex.ru', '{noop}password', 2005),
       ('Admin', 'admin@gmail.com', '{noop}admin', 1900),
       ('Guest', 'guest@gmail.com', '{noop}guest', 2000);

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name, address)
VALUES  ('Токио City', 'Просвещения 72'),
        ('BAHROMA', 'Просвещения 48'),
        ('Бургер Кинг', 'Энгельса 154'),
        ('Додо Пицца', 'Художников 26');

INSERT INTO DISH (created_date, name, price, rest_id)
VALUES ('2020-01-30', 'Мисо суп', 330, 1),
       ('2020-01-30', 'Ролл Филадельфия', 1000, 1),
       ('2020-01-30', 'Пуэр', 500, 1),
       ('2020-01-30', 'Домашний супчик', 400, 2),
       ('2020-01-31', 'Плов узбекский', 250, 2),
       ('2020-01-31', 'Бургер из говядины', 200, 3),
       ('2020-01-31', 'Гриль Кинг', 350, 3),
       ('2020-01-31', 'Кола', 100, 3);