CREATE SCHEMA IF NOT EXISTS cats;

DROP TABLE IF EXISTS cats."cat_view";

CREATE TABLE IF NOT EXISTS cats."cat_view"
(
    user_id         BIGINT      NOT NULL        REFERENCES bot."users"(id),
    cat_id          UUID        NOT NULL        REFERENCES cats."cats"(id),
    is_like         BOOLEAN     NOT NULL,
    viewed_times    BIGINT      NOT NULL,
    viewed_at       TIMESTAMP   NOT NULL,

    PRIMARY KEY(user_id, cat_id)
);