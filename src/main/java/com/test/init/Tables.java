package com.test.init;

import com.test.db.Session;
import java.util.List;
import org.cactoos.list.ListOf;

public class Tables {

    private static final List<String> TABLES = new ListOf<>(
        "CREATE TABLE IF NOT EXISTS account(\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name text,\n" +
            "    balance float\n" +
            ")",
        "CREATE TABLE IF NOT EXISTS operation(\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    from_acc account,\n" +
            "    to_acc account,\n" +
            "    amount FLOAT\n" +
            ")"
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
}
