CREATE SCHEMA IF NOT EXISTS cats;

DROP TABLE IF EXISTS cats."cats";

CREATE TABLE IF NOT EXISTS cats."cats"
(
    id                  UUID            PRIMARY KEY NOT NULL,
    name                VARCHAR(255)                NOT NULL,
    file_id             VARCHAR(255)                NOT NULL,
    creator_username    VARCHAR(255)                NOT NULL,
    creator_id          BIGINT                      NOT NULL,
    likes_count         BIGINT                      NOT NULL DEFAULT 0,
    dislikes_count      BIGINT                      NOT NULL DEFAULT 0,
    created             TIMESTAMPTZ                 NOT NULL
);