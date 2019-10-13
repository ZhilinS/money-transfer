package com.test.db;

import java.util.ArrayList;
import java.util.List;
import org.flywaydb.core.Flyway;
import org.sqlite.SQLiteDataSource;

public final class Connect {

    private static final List<Session> SESSIONS = new ArrayList<>(1);

    private final String url;

    public Connect(
        final String url
    ) {
        this.url = url;
    }

    public void init() {
        Flyway.configure()
            .dataSource(this.url, "", "")
            .load()
            .migrate();
    }

    public Session session() {
        if (Connect.SESSIONS.isEmpty()) {
            final SQLiteDataSource source = new SQLiteDataSource();
            source.setUrl(this.url);
            final Session session = new Session(source);
            Connect.SESSIONS.add(session);
            return session;
        } else {
            return Connect.SESSIONS.get(0);
        }
    }

    public void clean() {
        Flyway.configure()
            .dataSource(this.url, "", "")
            .load()
            .clean();
    }
}
