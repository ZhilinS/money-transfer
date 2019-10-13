package com.test.init;

import com.test.db.Session;
import java.util.List;
import org.cactoos.list.ListOf;

public class Tables {

    private static final List<String> TABLES = new ListOf<>(
        "CREATE TABLE IF NOT EXISTS account(\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name TEXT,\n" +
            "    balance DOUBLE\n" +
            ")",
        "CREATE TABLE IF NOT EXISTS operation(\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    from_acc account,\n" +
            "    to_acc account,\n" +
            "    amount DOUBLE\n" +
            ")"
    );

    private static final List<String> CLEAN = new ListOf<>(
        "DROP TABLE IF EXISTS account",
        "DROP TABLE IF EXISTS operation"
    );

    private final Session session;

    public Tables(final Session session) {
        this.session = session;
    }

    public void init() {
        Tables.TABLES.forEach(
            sql -> this.session.execute(ctx -> ctx.execute(sql))
        );
    }

    public void clean() {
        Tables.CLEAN.forEach(
            sql -> this.session.execute(ctx -> ctx.execute(sql))
        );
    }
}
