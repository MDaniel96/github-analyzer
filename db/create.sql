DROP TABLE aftweb.analyzer_repository;
DROP TABLE aftweb.analyzer_commit;
DROP TABLE aftweb.analyzer_contribution;

CREATE TABLE aftweb.analyzer_repository
(
    id           NUMBER,
    url          VARCHAR2(300) UNIQUE,
    name         VARCHAR2(100),
    last_queried DATE,

    PRIMARY KEY (id)
);

CREATE TABLE aftweb.analyzer_commit
(
    id            NUMBER,
    author        VARCHAR2(100),
    created       DATE,
    lines_added   NUMBER,
    lines_deleted NUMBER,
    repository_id NUMBER NOT NULL,

    PRIMARY KEY (id)
    /* FOREIGN KEY(repository_id) REFERENCES aftweb.analyzer_repository(id) */
);

CREATE TABLE aftweb.analyzer_contribution
(
    id             NUMBER,
    developer_name VARCHAR2(100),
    commits        NUMBER,
    total_commits  NUMBER,
    repository_url VARCHAR2(300),

    PRIMARY KEY (id)
);

CREATE TABLE aftweb.analyzer_modification
(
    id             NUMBER,
    year           NUMBER,
    month          NUMBER,
    added_lines    NUMBER,
    removed_lines  NUMBER,
    repository_url VARCHAR2(300),

    PRIMARY KEY (id)
);