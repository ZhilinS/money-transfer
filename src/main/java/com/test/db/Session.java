/*
 * Copyright (C) 2018, SEMRUSH CY LTD or it's affiliates
 */
package com.test.db;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public final class Session {

    private final DataSource source;

    public Session(final DataSource source) {
        this.source = source;
    }

    public void execute(final Consumer<DSLContext> code) {
        try (
            DSLContext create = DSL.using(
                this.source,
                SQLDialect.SQLITE
            )
        ) {
            code.accept(create);
        }
    }

    public <T> T retrieve(final Function<DSLContext, T> code) {
        try (
            final DSLContext create = DSL.using(
                this.source,
                SQLDialect.SQLITE
            )
        ) {
            return code.apply(create);
        }
    }
}
