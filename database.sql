-- CREATE
--     DATABASE ewallet;
-- open the database

-- ENUM TYPE
CREATE TYPE owner_type AS ENUM ('user', 'partner');
CREATE TYPE transaction_type AS ENUM ('deposit', 'withdrawal', 'transfer');
CREATE TYPE transaction_status AS ENUM ('pending', 'completed', 'failed', 'funded');
CREATE TYPE payment_system_type AS ENUM ('internal', 'paypal', 'stripe', 'other');


CREATE TABLE wallet
(
    id         serial PRIMARY KEY,
    owner_type owner_type NOT NULL default 'user',
    currency   VARCHAR(3) NOT NULL default 'VND',
    owner_id   char(10),
    balance    numeric    NOT NULL default 0,
    constraint uk_wallet_owner unique (owner_type, owner_id),
    constraint fk_owner_id check ( owner_type = 'user' and owner_id in (select id
                                                                        from "user") or
                                   owner_type = 'partner' and owner_id in (select id
                                                                           from partner))
);


CREATE TABLE service
(
    id           int PRIMARY KEY,
    name         varchar(255) NOT NULL,
    service_type varchar(255) NOT NULL,
    api_key      varchar(255) NOT NULL
);

-- Partner

CREATE SEQUENCE partner_id_seq START 1;
CREATE OR REPLACE FUNCTION generate_partner_id() RETURNS char(10) AS
$$
BEGIN
    RETURN LPAD(CAST(1000000000 + nextval('partner_id_seq') AS TEXT), 10, '0');
END;
$$ LANGUAGE plpgsql;

CREATE TABLE partner
(
    id           char(10) PRIMARY KEY  default generate_partner_id(),
    name         varchar(255) NOT NULL,
    description  text,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    api_base_url VARCHAR(255) NOT NULL,
    api_key      VARCHAR(255) NOT NULL,
    balance      numeric      NOT NULL,
    created      date         NOT NULL DEFAULT CURRENT_DATE,
    constraint uk_partner_email unique (email)
);


-- User

CREATE TABLE "user"
(
    id           char(10) PRIMARY KEY,
    first_name   varchar(50)  NOT NULL,
    last_name    varchar(50)  NOT NULL,
    email        varchar(255) NOT NULL,
    user_name    varchar(50),
    avatar       varchar(255) NOT NULL,
    password     varchar(255) NOT NULL,
    dob          date         NOT NULL,
    is_active    boolean      NOT NULL DEFAULT true,
    is_verified  boolean      NOT NULL DEFAULT false,
    gender       boolean,
    created      date         NOT NULL DEFAULT CURRENT_DATE,
    address      varchar(255),
    phone_number varchar(10),
    job          varchar(255),
    check ( phone_number ~ '^[0-9]{10}$' ),
    check ( email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$' ),
    check ( user_name ~ '^[a-zA-Z0-9._%+-]{6,}$' ),
    check ( dob <= CURRENT_DATE)
);

create index user_index on "user" (email, user_name, phone_number);
create index user_email_index on "user" (email);
create index user_un_index on "user" (user_name);
create index user_pn_index on "user" ( phone_number);

create table user_partner
(
    user_id    char(10) REFERENCES "user" (id),
    partner_id char(10) REFERENCES partner (id),
    PRIMARY KEY (user_id, partner_id),
    unique (user_id, partner_id),
    unique (partner_id)
);



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
/*CREATE OR REPLACE TRIGGER user_id_trigger
    BEFORE INSERT
    ON "user"
    FOR EACH ROW
EXECUTE FUNCTION generate_user_id();*/



CREATE OR REPLACE FUNCTION check_user_unique()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN

    IF NEW.partner_id is null then
        IF EXISTS (SELECT 1 FROM "user" WHERE partner_id = new.partner_id AND email = NEW.email) THEN
            RAISE EXCEPTION 'Email đã tồn tại';
        END IF;
        IF EXISTS (SELECT 1 FROM "user" WHERE partner_id = new.partner_id AND phone_number = NEW.phone_number) THEN
            RAISE EXCEPTION 'Số điện thoại đã tồn tại';
        END IF;
        IF EXISTS (SELECT 1 FROM "user" WHERE partner_id = new.partner_id AND user_name = NEW.user_name) THEN
            RAISE EXCEPTION 'Tên đăng nhập đã tồn tại';
        END IF;
    end if;


    NEW.id := LPAD(NEXTVAL('user_id_seq')::text, 10, '0');

    RETURN new;
END ;
$$;

CREATE OR REPLACE TRIGGER user_unique_trigger
    BEFORE INSERT
    ON "user"
    FOR EACH ROW
EXECUTE FUNCTION check_user_unique();


CREATE TABLE role
(
    id   int PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Employee

CREATE TABLE employee
(
    id      char(10) PRIMARY KEY REFERENCES "user" (id),
    salary  decimal(10, 2) NOT NULL,
    ssn     varchar(15)    NOT NULL UNIQUE,
    role_id int REFERENCES role (id)
);



CREATE TABLE payment_method
(
    id          int PRIMARY KEY,
    name        varchar(50) NOT NULL,
    description varchar(255)
);

-- Order table
CREATE TABLE transaction
(
    id                      char(15) PRIMARY KEY,
    payment_method_id       int REFERENCES payment_method (id),
    from_wallet_id          serial,
    to_wallet_id            serial,
    money                   decimal(10, 2)     NOT NULL,
    currency                VARCHAR(3)         NOT NULL,
    transaction_type        transaction_type   NOT NULL,
    status                  transaction_status NOT NULL default 'pending',
    type                    VARCHAR(50)        NOT NULL,
    external_transaction_id VARCHAR(255),
    payment_system_id       INT,
    timestamp               TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_wallet_id) REFERENCES wallet (id),
    FOREIGN KEY (to_wallet_id) REFERENCES wallet (id),
    FOREIGN KEY (payment_system_id) REFERENCES payment_system (id)
);

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

CREATE TABLE payment_system
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50)         NOT NULL,
    type       payment_system_type NOT NULL,
    api_key    VARCHAR(255),
    api_secret VARCHAR(255),
    is_active  BOOLEAN DEFAULT true
);


CREATE TABLE "order"
(
    id                      varchar(15) PRIMARY KEY,
    service_id              int REFERENCES service (id),
    money                   decimal(10, 2) NOT NULL,
    status                  varchar(50)    NOT NULL,
    invoice_id              varchar(50)    NULL UNIQUE,
    transaction_id          varchar(15)    NULL REFERENCES transaction (id),
    payment_method          int REFERENCES payment_method (id),
    external_transaction_id VARCHAR(255) REFERENCES payment_system (id),
    created                 date           NOT NULL DEFAULT CURRENT_DATE,
    updated                 date           NOT NULL DEFAULT CURRENT_DATE
);

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


-- Support Ticket
CREATE TABLE support_ticker
(
    id          varchar(15) PRIMARY KEY,
    customer_id char(10) REFERENCES customer (id),
    title       varchar(255) NOT NULL,
    content     text         NOT NULL,
    status      varchar(50)  NOT NULL
);

-- Group Fund
CREATE TABLE group_fund
(
    id          int PRIMARY KEY,
    name        varchar(255)   NOT NULL,
    description varchar(255),
    balance     decimal(10, 2) NOT NULL,
    target      decimal(10, 2) NOT NULL,
    owner_id    char(10) REFERENCES "user" (id)
);


CREATE TABLE fund_member
(
    group_id  int REFERENCES group_fund (id),
    member_id char(10) REFERENCES "user" (id),
    money     numeric NOT NULL DEFAULT 0,
    PRIMARY KEY (group_id, member_id)
);


CREATE TABLE financial_statistic

(
    id          INT PRIMARY KEY,
    creator     CHAR(10) REFERENCES "employee" (id),
    profit      numeric NOT NULL,
    income      numeric NOT NULL,
    outcome     numeric NOT NULL,
    total_money numeric NOT NULL,
    created     DATE    NOT NULL DEFAULT CURRENT_DATE
);


CREATE TABLE customer_statistic
(
    month    int                               NOT NULL,
    year     int                               NOT NULL,
    customer char(10) REFERENCES customer (id) NOT NULL,
    income   numeric                           NOT NULL,
    outcome  numeric                           NOT NULL,
    PRIMARY KEY (month, year, customer)
);


CREATE TABLE service_statistic
(
    month   int                         NOT NULL,
    year    int                         NOT NULL,
    service int REFERENCES service (id) NOT NULL,
    income  numeric                     NOT NULL,
    outcome numeric                     NOT NULL,
    PRIMARY KEY (month, year, service)
);

