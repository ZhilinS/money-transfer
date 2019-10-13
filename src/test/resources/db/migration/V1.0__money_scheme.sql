CREATE TABLE IF NOT EXISTS account
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name    TEXT,
    balance DOUBLE
);
CREATE TABLE operation
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    from_acc INTEGER,
    to_acc   INTEGER,
    amount   DOUBLE,
    status   TEXT,
    FOREIGN KEY (from_acc) REFERENCES account (id),
    FOREIGN KEY (to_acc) REFERENCES account (id)
);