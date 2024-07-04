create database ewallet;
-- open the database


create table service
(
    id     int primary key,
    name   varchar(255) not null,
    apiKey varchar(255) not null
);


create table "user"
(
    id         char(10) primary key,
    firstName  varchar(50)  not null,
    lastName   varchar(50)  not null,
    email      varchar(255) not null,
    username   varchar(50),
    password   varchar(255) not null,
    dob        date         not null,
    gender     boolean,
    created    date         not null default current_date,
    address    varchar(255),
    phone      varchar(10),
    job        varchar(255),
    service_id int references service (id),
    UNIQUE (service_id, email),
    UNIQUE (service_id, phone),
    unique (service_id, username)
);

create table customer
(
    id char(10) primary key references "user" (id)
);

create table role
(
    id   int primary key,
    name varchar(50) not null
);


create table employee
(
    id      char(10) primary key references "user" (id),
    salary  decimal(10, 2) not null,
    role_id int references role (id)
);

create table wallet
(
    id       varchar(255) primary key,
    owner_id char(10) references "user" (id),
    balance  decimal(10, 2) not null
);

create table transaction
(
    id        varchar(255) primary key,
    wallet_id varchar(255) references wallet (id),
    amount    decimal(10, 2) not null,
    type      varchar(50)    not null,
    created   date           not null
);

