CREATE TABLE IF NOT EXISTS users
(
    user_uuid uuid UNIQUE PRIMARY KEY DEFAULT gen_random_uuid(),
    email     VARCHAR(255) UNIQUE NOT NULL,
    name      VARCHAR(50)         NOT NULL,
    bio TEXT DEFAULT ''
);

CREATE TABLE IF NOT EXISTS papers
(
    paper_uuid uuid UNIQUE PRIMARY KEY DEFAULT gen_random_uuid(),
    title      VARCHAR(255) NOT NULL,
    abstract   TEXT,
    file       TEXT,
    doi        VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS authors
(
    author_uuid uuid UNIQUE PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS tags
(
    tag        VARCHAR(50) UNIQUE PRIMARY KEY UNIQUE NOT NULL,
    parent_tag VARCHAR(50) REFERENCES tags (tag)
);

CREATE TABLE IF NOT EXISTS paper_authors
(
    author_uuid uuid UNIQUE REFERENCES authors (author_uuid),
    paper_uuid  uuid UNIQUE REFERENCES paper_authors (paper_uuid),
    PRIMARY KEY (author_uuid, paper_uuid)
);

CREATE TABLE IF NOT EXISTS paper_owners
(
    user_uuid  uuid UNIQUE REFERENCES users (user_uuid),
    paper_uuid uuid UNIQUE REFERENCES papers (paper_uuid),
    PRIMARY KEY (user_uuid, paper_uuid)
);

CREATE TABLE IF NOT EXISTS paper_tags
(
    tag        VARCHAR(50) UNIQUE REFERENCES tags (tag),
    paper_uuid uuid UNIQUE REFERENCES papers (paper_uuid),
    PRIMARY KEY (tag, paper_uuid)
);

INSERT INTO users (email, name)
VALUES ('test@nau.edu', 'Test User')
RETURNING *;


INSERT INTO users (email, name)
VALUES ('test2@nau.edu', 'Test2 User')
RETURNING *;
