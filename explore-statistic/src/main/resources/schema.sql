DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS endpoint_hit
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    app
    VARCHAR
    NOT
    NULL,
    uri
    VARCHAR
    NOT
    NULL,
    ip
    VARCHAR
    NOT
    NULL,
    timestamp
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    CONSTRAINT
    pk_requests
    PRIMARY
    KEY
(
    id
)
    );
