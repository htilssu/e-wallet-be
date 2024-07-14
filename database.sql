CREATE
    DATABASE ewallet;
-- open the database

CREATE TABLE service
(
    id           int PRIMARY KEY,
    name         varchar(255) NOT NULL,
    service_type varchar(255) NOT NULL,
    api_key      varchar(255) NOT NULL);

-- User
CREATE TABLE "user"
(
    id           char(10) PRIMARY KEY,
    first_name   varchar(50)  NOT NULL,
    last_name    varchar(50)  NOT NULL,
    email        varchar(255) NOT NULL,
    user_name    varchar(50),
    password     varchar(255) NOT NULL,
    dob          date         NOT NULL,
    gender       boolean,
    created      date         NOT NULL DEFAULT CURRENT_DATE,
    address      varchar(255),
    phone_number varchar(10),
    job          varchar(255),
    service_id   int REFERENCES service (id),
    UNIQUE (service_id, email),
    UNIQUE (service_id, phone_number),
    UNIQUE (service_id, user_name));

CREATE INDEX user_index ON "user" (service_id, email, phone_number, user_name);

-- user id generation
CREATE SEQUENCE user_id_seq START 1000000001;
-- drop sequence user_id_seq;

CREATE OR REPLACE FUNCTION generate_user_id()
    RETURNS TRIGGER
AS
$$
BEGIN
    NEW.id := LPAD(NEXTVAL('user_id_seq')::text, 10, '0');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger gen id trước khi thêm vào db
CREATE OR REPLACE TRIGGER user_id_trigger
    BEFORE INSERT
    ON "user"
    FOR EACH ROW
EXECUTE FUNCTION generate_user_id();

CREATE OR REPLACE FUNCTION check_user_unique()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF NEW.service_id IS NULL THEN
        IF EXISTS ( SELECT 1 FROM "user" WHERE email = NEW.email ) THEN
            RAISE EXCEPTION 'Email đã tồn tại';
            END IF;
        IF EXISTS ( SELECT 1 FROM "user" WHERE phone_number = NEW.phone_number ) THEN
            RAISE EXCEPTION 'Số điện thoại đã tồn tại';
            END IF;
        IF EXISTS ( SELECT 1 FROM "user" WHERE user_name = NEW.user_name ) THEN
            RAISE EXCEPTION 'Tên đăng nhập đã tồn tại';
            END IF;
        END IF;

    RETURN new;
END ;
$$;

CREATE OR REPLACE TRIGGER user_unique_trigger
    BEFORE INSERT
    ON "user"
    FOR EACH ROW
EXECUTE FUNCTION check_user_unique();

-- Customer
CREATE TABLE customer
(
    id        char(10) PRIMARY KEY REFERENCES "user" (id),
    is_verify boolean NOT NULL DEFAULT FALSE);


CREATE TABLE role
(
    id   int PRIMARY KEY,
    name varchar(50) NOT NULL);

-- Employee

CREATE TABLE employee
(
    id      char(10) PRIMARY KEY REFERENCES "user" (id),
    salary  decimal(10, 2) NOT NULL,
    ssn     varchar(15)    NOT NULL UNIQUE ,
    role_id int REFERENCES role (id));


CREATE TABLE wallet
(
    id                varchar(255) PRIMARY KEY,
    owner_id          char(10) REFERENCES customer (id),
    balance           numeric NOT NULL);


CREATE TABLE payment_method
(
    id          int PRIMARY KEY,
    name        varchar(50) NOT NULL,
    description varchar(255));


-- Order table


CREATE TABLE "order"
(
    id             varchar(15) PRIMARY KEY,
    service_id     int REFERENCES service (id),
    money          decimal(10, 2) NOT NULL,
    status         varchar(50)    NOT NULL,
    invoice_id     varchar(50)    NULL UNIQUE,
    transaction_id varchar(15)    NULL REFERENCES transaction (id),
    payment_method int REFERENCES payment_method (id),
    created        date           NOT NULL DEFAULT CURRENT_DATE,
    updated        date           NOT NULL DEFAULT CURRENT_DATE);

CREATE SEQUENCE order_id_seq START 100000000000001;

CREATE OR REPLACE FUNCTION generate_order_id()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.id := LPAD(NEXTVAL('order_id_seq')::text, 15, '0');
    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER order_id_trigger
    BEFORE INSERT
    ON "order"
    FOR EACH ROW
EXECUTE FUNCTION generate_order_id();
/*
CREATE OR REPLACE FUNCTION check_order_transaction()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$

BEGIN
    IF NEW.status <> OLD.status THEN
        IF NEW.status = 'SUCCESS' THEN
--     check count transaction with order if
            IF (new.transaction_id IS NOT NULL) THEN
                RETURN new;
                ELSE
                    RAISE EXCEPTION 'Đơn hàng chưa được thanh toán, không thể chuyển trạng thái thành công';
                END IF;
            END IF;
        END IF;
    RETURN NEW;
END;
$$;*/

/*CREATE OR REPLACE TRIGGER check_order_transaction
    BEFORE UPDATE OR INSERT
    ON "order"
    FOR EACH ROW
EXECUTE FUNCTION check_order_transaction();*/

-- Transaction table

CREATE TABLE transaction
(
    id                char(15) PRIMARY KEY,
    wallet_id         VARCHAR(255) REFERENCES wallet (id),
    payment_method_id int REFERENCES payment_method (id),
    money             decimal(10, 2) NOT NULL,
    type              VARCHAR(50)    NOT NULL,
    created           DATE           NOT NULL);

CREATE SEQUENCE transaction_id_seq START 100000000000001;

CREATE OR REPLACE FUNCTION generate_transaction_id()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.id := LPAD(NEXTVAL('transaction_id_seq')::text, 15, '0');
    RETURN NEW;
END;
$$;

-- Support Ticket
CREATE TABLE support_ticker
(
    id          varchar(15) PRIMARY KEY,
    customer_id char(10) REFERENCES customer (id),
    title       varchar(255) NOT NULL,
    content     text         NOT NULL,
    status      varchar(50)  NOT NULL);

-- Group Fund
CREATE TABLE group_fund
(
    id          int PRIMARY KEY,
    name        varchar(255)   NOT NULL,
    description varchar(255),
    balance     decimal(10, 2) NOT NULL,
    target      decimal(10, 2) NOT NULL,
    owner_id    char(10) REFERENCES "user" (id));


CREATE TABLE fund_member
(
    group_id  int REFERENCES group_fund (id),
    member_id char(10) REFERENCES "user" (id),
    money     numeric NOT NULL DEFAULT 0,
    PRIMARY KEY (group_id, member_id));


CREATE TABLE financial_statistic

(
    id          INT PRIMARY KEY,
    creator     CHAR(10) REFERENCES "employee" (id),
    profit      numeric NOT NULL,
    income      numeric NOT NULL,
    outcome     numeric NOT NULL,
    total_money numeric NOT NULL,
    created     DATE    NOT NULL DEFAULT CURRENT_DATE);


CREATE TABLE customer_statistic
(
    month    int                               NOT NULL,
    year     int                               NOT NULL,
    customer char(10) REFERENCES customer (id) NOT NULL,
    income   numeric                           NOT NULL,
    outcome  numeric                           NOT NULL,
    PRIMARY KEY (month, year, customer));


CREATE TABLE service_statistic
(
    month   int                         NOT NULL,
    year    int                         NOT NULL,
    service int REFERENCES service (id) NOT NULL,
    income  numeric                     NOT NULL,
    outcome numeric                     NOT NULL,
    PRIMARY KEY (month, year, service));


create table partner
(
    id          int PRIMARY KEY,
    name        varchar(255) NOT NULL,
    description text,
    service_id  int REFERENCES service (id),
    api_key     varchar(255) NOT NULL,
    balance     numeric NOT NULL,
    created     date NOT NULL DEFAULT CURRENT_DATE);



