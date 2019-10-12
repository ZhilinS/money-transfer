/*
 * Copyright (C) 2018, SEMRUSH CY LTD or it's affiliates
 */
package com.test.db;

import java.util.ArrayList;
import java.util.List;
import org.sqlite.SQLiteDataSource;

public final class Connect {

    private static final List<Session> SESSIONS = new ArrayList<>(1);

    private final String url;

    public Connect(
        final String url
    ) {
        this.url = url;
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
}
