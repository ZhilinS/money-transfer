package com.test;

import com.google.gson.Gson;
import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.http.routes.get.AccountGet;
import com.test.http.routes.post.AccountPost;
import com.test.init.Tables;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.http.HttpStatus;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferTesting {

    private final static int PORT = 8081;

    private Tables tables;

    @BeforeAll
    public void setup() {
        final Session session = new Connect("jdbc:sqlite:memory:test").session();
        tables = new Tables(session);
        tables.init();
        new Router(
            TransferTesting.PORT,
            new AccountGet(
                new AccountSingle(
                    session
                )
            ),
            new AccountPost(
                new AccountInsert(
                    session
                )
            )
        ).init();
    }

    @Test
    @Order(1)
    public void testPostAccount() throws IOException {
        new Gson()
            .fromJson(
                new TextOf(
                    new ResourceOf(
                        Paths.get("accounts.json").toString()
                    )
                ).asString(),
                List.class
            )
            .forEach(
                acc -> given()
                    .body(new Gson().toJson(acc))
                    .when()
                    .port(TransferTesting.PORT)
                    .post("/api/account")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
            );
    }

    @Test
    @Order(2)
    public void testGetAccount() {
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/1")
            .then()
            .assertThat()
            .body("name", equalTo("Imogen Wickens"))
            .statusCode(HttpStatus.SC_OK);
    }

    @AfterAll
    public void clean() {
        tables.clean();
    }
}
