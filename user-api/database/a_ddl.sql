
create table app_user (
    app_user_id char(36) primary key,
    username varchar(255) not null unique,
    password_hash varchar(2048) not null,
    disabled boolean not null default(0),
    first_name varchar(50) null,
    last_name varchar(50) null,
    email_address varchar(255) null,
    mobile_phone varchar(20) null
);

create table app_role (
    app_role_id int primary key auto_increment,
    app_role_name varchar(50) not null unique
);

create table app_user_role (
    app_user_id char(36) not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);
