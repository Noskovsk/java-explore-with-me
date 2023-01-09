CREATE TABLE IF NOT EXISTS endpoint_hit
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       VARCHAR(255)                            NOT NULL,
    uri       VARCHAR(2000)                           NOT NULL,
    ip        VARCHAR(255)                            NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);