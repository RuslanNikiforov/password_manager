DELETE FROM app_passwords;
delete from user_roles;
delete from users;

INSERT INTO users (email, name, password)
VALUES ('example1@mail.com', 'Alex', '$2a$05$rcwKfTdM1AZBo02tfRRmc.9PL2KTyAsCKzAnB3NOBYMoJ3CM4EqT.'),
        ('example2@mail.com', 'Misha', '$2a$05$SAdwkv4GXf0jUuSPppNJz.eRILbyPd.fcis3IjlT.yfsVY9WWNhvK'),
    ('example3@mail.com', 'Ruslan', '$2a$05$I0yAgHFcjlJnmRb1dqLSxuv664NA9IhGIlZr5Eq9Ux57ge3n6sWHW'),
    ('example4@mail.com', 'Dasha', '$2a$05$2yfvxQzLW0vFzDtXBlFtROhD2ujvUrOMPBKgF5a9Xv6nYHiQlUanq'),
    ('example5@mail.com', 'Victor', '$2a$05$NTHHpxz3tvrnLThdcd9oyOa1IqKicnSj.Qx7a2wOZ63qlS4BJNfMy');

INSERT INTO app_passwords (app_name,  password, user_id)
VALUES ('Steam', 'kSfOMKECitlLqYX8AxhNMw==', 1),
       ('AliExpress', 'Cx5GuQ2dWZt/f2KqVJg3Sw==', 1),
       ('Mail.ru', 'xPMezRKlRtjwhE9NFv84LBOAb/GR1y2P', 4),
       ('Google.com', 'SuYPJ/sCWaen60iTLEmt8fqn4KAHYOLD', 4),
       ('test.com', 'XETfSmUDiXYP1k4p559Qqo47HyHKqgF3', 4),
       ('Steam', 'vVaG0TqQJKsTR6UExG29ww==', 2);


INSERT INTO user_roles (role, user_id) VALUES ('USER', 1), ('ADMIN', 1),
                                              ('USER', 2),
                                              ('USER', 3),
                                              ('USER', 4),
                                              ('USER', 5);