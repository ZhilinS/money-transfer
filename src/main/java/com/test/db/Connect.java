package com.test.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import org.flywaydb.core.Flyway;

public final class Connect {

    private final String url;
    private final Integer pool;

    public Connect(
        final String url,
        final Integer pool
    ) {
        this.url = url;
        this.pool = pool;
    }

    public void init() {
        Flyway.configure()
            .dataSource(this.url, "", "")
            .load()
            .migrate();
    }

    public Session session() {
        final Properties props = new Properties();
        props.setProperty("jdbcUrl", this.url);
        props.setProperty("maximumPoolSize", String.valueOf(this.pool));
        return new Session(
            new HikariDataSource(
                new HikariConfig(props)
            )
        );
    }

    public void clean() {
        Flyway.configure()
            .dataSource(this.url, "", "")
            .load()
            .clean();
    }
}
