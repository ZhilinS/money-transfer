CREATE TABLE IF NOT EXISTS transaction(
    id INTEGER,
    from_acc account,
    to_acc account,
    amount FLOAT
)