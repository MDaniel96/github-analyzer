DROP TABLE aftweb.analyzer_repository;
DROP TABLE aftweb.analyzer_commit;

CREATE TABLE aftweb.analyzer_repository
(
    id           NUMBER,
    url          VARCHAR2(50) UNIQUE,
    name         VARCHAR2(50),
    last_queried DATE,

    PRIMARY KEY (id)
);

CREATE TABLE aftweb.analyzer_commit
(
    id            NUMBER,
    author        VARCHAR2(50),
    created       DATE,
    lines_added   NUMBER,
    lines_deleted NUMBER,
    repository_id NUMBER NOT NULL,

    PRIMARY KEY (id)
    /* FOREIGN KEY(repository_id) REFERENCES aftweb.analyzer_repository(id) */
);