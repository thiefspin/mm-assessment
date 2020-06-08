DROP SCHEMA IF EXISTS ussd CASCADE;
CREATE SCHEMA ussd;

CREATE TABLE ussd.countries
(
    id             serial PRIMARY KEY,
    name           VARCHAR NOT NULL,
    currency_code  VARCHAR NOT NULL,
    currency_value decimal NOT NULL
);

CREATE TABLE ussd.sessions
(
    id           serial PRIMARY KEY,
    session_id   VARCHAR   NOT NULL,
    msisdn       VARCHAR   NOT NULL,
    current_menu VARCHAR   NOT NULL,
    country_id   int REFERENCES ussd.countries (id),
    amount       DECIMAL,
    completed    BOOLEAN   NOT NULL,
    last_updated TIMESTAMP NOT NULL
);

INSERT INTO ussd.countries (name, currency_code, currency_value)
VALUES ('Kenya', 'KES', 6.10), ('Malawi', 'MWK', 42.50);