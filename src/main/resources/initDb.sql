drop table if exists user_code_reset_password;
drop table if exists app_passwords;
drop table if exists user_roles;
drop table if exists users;


create table users
(id  serial not null primary key,
 email varchar(255) not null ,
 name varchar(255) not null ,
 password varchar(255) not null
);

create table app_passwords
(
    id  serial not null primary key,
    app_name varchar(255) not null ,
    last_modified timestamp DEFAULT now() not null,
    password varchar(255) not null,
    user_id Integer NOT NULL,
    foreign key (user_id) references users(id) ON DELETE CASCADE
);

CREATE TABLE user_roles
(
    role VARCHAR(255) NOT NULL,
    user_id Integer NOT NULL,
    foreign key (user_id) references users(id) ON DELETE CASCADE
);

CREATE TABLE user_code_reset_password(
    id serial not null primary key,
    code VARCHAR(255) NOT NULL,
    sent_time timestamp NOT NULL,
    user_id INTEGER NOT NULL,
    foreign key (user_id) references users(id) ON DELETE CASCADE
);

CREATE TABLE auth_token_2fa(
                                         id serial not null primary key,
                                         token VARCHAR(255) NOT NULL,
                                         sent_time timestamp NOT NULL,
                                         user_id INTEGER NOT NULL,
                                         foreign key (user_id) references users(id) ON DELETE CASCADE
);

CREATE unique index unique_appName on app_passwords(app_name, user_id);
CREATE unique index unique_email on users(email);
CREATE unique index unique_code on user_code_reset_password(code);
CREATE unique index unique_user on user_code_reset_password(user_id);
CREATE unique index token on auth_token_2fa(token);
CREATE unique index unique_user_token on auth_token_2fa(user_id);
