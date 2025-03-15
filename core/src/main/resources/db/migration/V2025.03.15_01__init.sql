-- Создание таблицы user_data
CREATE TABLE user_data
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(500) NOT NULL,
    date_of_birth DATE         NOT NULL,
    password      VARCHAR(500) NOT NULL
);

-- Создание таблицы account
CREATE TABLE account
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    users_id BIGINT         NOT NULL UNIQUE,
    balance  DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (users_id) REFERENCES user_data (id)
);

-- Создание таблицы email_data
CREATE TABLE email_data
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    users_id BIGINT       NOT NULL,
    email    VARCHAR(200) NOT NULL UNIQUE,
    CONSTRAINT fk_email_data_user FOREIGN KEY (users_id) REFERENCES user_data (id)
);

-- Создание таблицы phone_data
CREATE TABLE phone_data
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    users_id BIGINT      NOT NULL,
    phone    VARCHAR(13) NOT NULL UNIQUE,
    CONSTRAINT fk_phone_data_user FOREIGN KEY (users_id) REFERENCES user_data (id)
);

CREATE INDEX idx_email_data_email ON email_data(email);
CREATE INDEX idx_phone_data_phone ON phone_data(phone);