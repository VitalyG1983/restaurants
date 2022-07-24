INSERT INTO USERS (NAME, EMAIL, PASSWORD, CALORIES_PER_DAY)
VALUES ('User', 'user@yandex.ru', '{noop}password', 2005),
       ('Admin', 'admin@gmail.com', '{noop}admin', 1900),
       ('Guest', 'guest@gmail.com', '{noop}guest', 2000);

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name, address)
VALUES ('Токио City', 'Просвещения 72'),
       ('BAHROMA', 'Просвещения 48'),
       ('Бургер Кинг', 'Энгельса 154'),
       ('Додо Пицца', 'Художников 26');

INSERT INTO VOTE (dt_vote, date_vote, user_id, rest_id)
VALUES ('2020-01-30 10:00', '2020-01-30', 1, 3),
       ('2020-01-31 13:00', '2020-01-31', 2, 2);

INSERT INTO DISH (name, price, rest_id)
VALUES ('Мисо суп', 330, 1),
       ('Ролл Филадельфия', 1000, 1),
       ('Пуэр', 500, 1),
       ('Домашний супчик', 400, 2),
       ('Плов узбекский', 250, 2),
       ('Бургер из говядины', 200, 2),
       ('Гриль Кинг', 350, 3),
       ('Кола', 100, 3);

/*INSERT INTO MENU (menu_date, rest_id)
VALUES ('2020-01-30', 1),
       ('2020-01-30', 2),
       ('2020-01-31', 3);*/

/*INSERT INTO DISH (name, price, rest_id, menu_id)
VALUES ('Мисо суп', 330, 1, 1),
       ('Ролл Филадельфия', 1000, 1, 1),
       ('Пуэр', 500, 1, 1),
       ('Домашний супчик', 400, 2, 2),
       ('Плов узбекский', 250, 2, 2),
       ('Бургер из говядины', 200, 3, 3),
       ('Гриль Кинг', 350, 3, 3),
       ('Кола', 100, 3, 3);*/

INSERT INTO MENU (menu_date, rest_id, dish_id)
  VALUES ('2020-01-30', 1, 1),
       ('2020-01-31', 1, 2),
       ('2020-01-29', 1, 3),
       ('2020-01-30', 2, 4),
       ('2020-01-31', 2, 5),
       ('2020-01-31', 2, 6),
       ('2020-01-31', 3, 7),
       ('2020-01-31', 3, 8);
