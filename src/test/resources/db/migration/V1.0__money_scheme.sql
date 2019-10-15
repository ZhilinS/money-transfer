CREATE TABLE IF NOT EXISTS account
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name    TEXT,
    balance DOUBLE
);
CREATE TABLE transfer
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    first    INTEGER,
    second   INTEGER,
    amount   DOUBLE,
    type     TEXT,
    status   TEXT,
    FOREIGN KEY (first)  REFERENCES account (id),
    FOREIGN KEY (second) REFERENCES account (id)
);
