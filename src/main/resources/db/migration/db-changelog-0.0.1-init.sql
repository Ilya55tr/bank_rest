-- liquibase formatted sql

-- changeset ilyatr:001-create-users-table
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(50)  NOT NULL UNIQUE,
    firstname  VARCHAR(20)  NOT NULL,
    lastname   VARCHAR(20)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- changeset ilyatr:002-create-cards-table
CREATE TABLE cards
(
    id                    BIGSERIAL PRIMARY KEY,
    public_number         uuid           NOT NULL UNIQUE,
    card_number_encrypted BYTEA          NOT NULL,
    last4                 VARCHAR(4)     NOT NULL,
    expiration_date       DATE           NOT NULL,
    status                VARCHAR(20)    NOT NULL,
    balance               NUMERIC(19, 2) NOT NULL CHECK (balance >= 0),
    owner_id              BIGINT         NOT NULL,
    created_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cards_owner
        FOREIGN KEY (owner_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_cards_owner_id ON cards (owner_id);
CREATE INDEX idx_cards_status ON cards (status);


-- changeset ilyatr:003-create-transactions-table
CREATE TABLE transactions
(
    id           BIGSERIAL PRIMARY KEY,
    from_card_id BIGINT         NOT NULL,
    to_card_id   BIGINT         NOT NULL,
    amount       NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    status       VARCHAR(20)    NOT NULL,
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tx_from_card
        FOREIGN KEY (from_card_id)
            REFERENCES cards (id),

    CONSTRAINT fk_tx_to_card
        FOREIGN KEY (to_card_id)
            REFERENCES cards (id),

    CONSTRAINT chk_cards_not_equal
        CHECK (from_card_id <> to_card_id)
);


CREATE INDEX idx_tx_from_card ON transactions (from_card_id);
CREATE INDEX idx_tx_to_card ON transactions (to_card_id);
CREATE INDEX idx_tx_created_at ON transactions (created_at);

--changeset ilyatr:004-create-card-request-table
CREATE TABLE card_requests
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT         NOT NULL,
    card_id           BIGINT         NULL,
    type              VARCHAR(20)    NOT NULL,
    requested_balance NUMERIC(19, 2) NULL,
    status            VARCHAR(20)    NOT NULL,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_card_request_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_card_request_card
        FOREIGN KEY (card_id)
            REFERENCES cards (id)
            ON DELETE SET NULL
);

CREATE INDEX idx_card_request_user_id ON card_requests (user_id);
CREATE INDEX idx_card_request_card_id ON card_requests (card_id);
CREATE INDEX idx_card_request_status ON card_requests (status);
