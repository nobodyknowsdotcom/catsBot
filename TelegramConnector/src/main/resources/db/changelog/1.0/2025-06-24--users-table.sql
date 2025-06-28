CREATE SCHEMA IF NOT EXISTS bot;

DROP TABLE IF EXISTS bot."users";

CREATE TABLE IF NOT EXISTS bot."users"
(
    id                  BIGINT       PRIMARY KEY NOT NULL,
    username            VARCHAR(255)             NOT NULL,
    state               VARCHAR(255)             NOT NULL,
    created             TIMESTAMPTZ              NOT NULL
);