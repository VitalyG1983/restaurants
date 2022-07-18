INSERT INTO USERS (NAME, EMAIL, PASSWORD, CALORIES_PER_DAY)
VALUES ('User', 'user@yandex.ru', '{noop}password', 2005),
       ('Admin', 'admin@gmail.com', '{noop}admin', 1900),
       ('Guest', 'guest@gmail.com', '{noop}guest', 2000);

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO DISH (created_date, name, price, user_id)
VALUES ('2020-01-30', 'Завтрак', 500, 1),
       ('2020-01-30', 'Обед', 1000, 1),
       ('2020-01-30', 'Ужин', 500, 1),
       ('2020-01-31', 'Еда на граничное значение', 100, 1),
       ('2020-01-31', 'Завтрак', 500, 1),
       ('2020-01-31', 'Обед', 1000, 1),
       ('2020-01-31', 'Ужин', 510, 1),
       ('2020-01-31', 'Админ ланч', 510, 2),
       ('2020-01-31', 'Админ ужин', 1500, 2);