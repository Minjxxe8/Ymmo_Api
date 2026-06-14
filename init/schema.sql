CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    name       VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE property_types
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE wallets
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER        NOT NULL,
    balance NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_sessions
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER      NOT NULL,
    token      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP    NOT NULL,
    revoked    BOOLEAN      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE properties
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255)   NOT NULL,
    type_id      INTEGER        NOT NULL,
    price        NUMERIC(10, 2) NOT NULL,
    surface_area NUMERIC(10, 2) NOT NULL,
    room_count   INTEGER        NOT NULL,
    diagnostic   VARCHAR(255)   NOT NULL,
    country      VARCHAR(255)   NOT NULL,
    city         VARCHAR(255)   NOT NULL,
    area         VARCHAR(255)   NOT NULL,
    on_sale      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (type_id) REFERENCES property_types (id)
);

CREATE TABLE transactions
(
    id          SERIAL PRIMARY KEY,
    description text           NOT NULL,
    user_id     INTEGER        NOT NULL,
    amount      NUMERIC(10, 2) NOT NULL,
    property_id INTEGER        NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE
);

CREATE TABLE favorites
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER NOT NULL,
    property_id INTEGER NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE
);

CREATE TABLE property_picture
(
    id          SERIAL PRIMARY KEY,
    path        VARCHAR(255),
    property_id INTEGER,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties (id) ON DELETE CASCADE
);
