--DROP SCHEMA public CASCADE;
--CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512) UNIQUE                     NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
) ^;

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) UNIQUE                     NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
) ^;

CREATE TABLE IF NOT EXISTS location
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
) ^;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'event_status') THEN
            CREATE TYPE event_status AS ENUM ('PENDING', 'PUBLISHED', 'CANCELED');
        END IF;
    END
$$ ^;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'request_status') THEN
            CREATE TYPE request_status AS ENUM ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELED');
        END IF;
    END
$$ ^;

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    created            TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    published          TIMESTAMP WITHOUT TIME ZONE,
    state              event_status,
    location_id        BIGINT                                  NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    paid               BOOLEAN DEFAULT false,
    participant_limit  BIGINT  DEFAULT 0,
    request_moderation BOOLEAN DEFAULT true,
    title              VARCHAR(120)                            NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT "fk_category_category_id" FOREIGN KEY (category_id)
        REFERENCES category (id) ON DELETE CASCADE,
    CONSTRAINT "fk_location_location_id" FOREIGN KEY (location_id)
        REFERENCES location (id) ON DELETE CASCADE,
    CONSTRAINT "fk_user_user_id" FOREIGN KEY (initiator_id)
        REFERENCES users (id) ON DELETE CASCADE
) ^;

CREATE TABLE IF NOT EXISTS participation_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    state        request_status,
    CONSTRAINT pk_part_req PRIMARY KEY (id),
    CONSTRAINT "fk_event_event_id" FOREIGN KEY (event_id)
        REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT "fk_user_user_id" FOREIGN KEY (requester_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT no_duplicate_request UNIQUE (requester_id, event_id)
) ^;

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(200)                            NOT NULL,
    CONSTRAINT pk_comp PRIMARY KEY (id)
) ^;

CREATE TABLE IF NOT EXISTS event_and_compilations
(
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT "pk_event_and_compilations" PRIMARY KEY ("event_id", "compilation_id"),
    CONSTRAINT "fk_events_event_id" FOREIGN KEY (event_id)
        REFERENCES events (id),
    CONSTRAINT "fk_compil_compil_id" FOREIGN KEY (compilation_id)
        REFERENCES compilations (id)
) ^;