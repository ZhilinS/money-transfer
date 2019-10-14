package com.test;

import com.google.gson.Gson;
import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.http.routes.Accounts;
import com.test.job.Reactor;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import com.test.query.AccountUpdate;
import com.test.query.OperationInsert;
import com.test.query.OperationUpdate;
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

    private Connect connect;

    @BeforeAll
    public void setup() {
        connect = new Connect("jdbc:sqlite:memory:test");
        connect.init();
        final Session session = connect.session();
        new Router(
            TransferTesting.PORT,
            new Accounts(
                new AccountSingle(session),
                new AccountInsert(session),
                new AccountUpdate(session),
                new Reactor(
                    new OperationUpdate(session),
                    new OperationInsert(session)
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
            .body("balance", equalTo(0.0f))
            .statusCode(HttpStatus.SC_OK);
    }

    //    @Test
    @Order(3)
    public void testWithdraw() {
        given()
            .body("{\"amount\": 12.3}")
            .when()
            .port(TransferTesting.PORT)
            .post("/api/account/withdraw/3");
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/3")
            .then()
            .assertThat()
            .body("balance", equalTo(87.7))
            .statusCode(HttpStatus.SC_OK);
    }

    //    @Test
    @Order(4)
    public void testDeposit() {
        given()
            .body("{\"amount\": 12.3}")
            .when()
            .port(TransferTesting.PORT)
            .post("/api/account/deposit/3");
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/3")
            .then()
            .assertThat()
            .body("balance", equalTo(100.0))
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(5)
    public void testTransfer() {
        given()
            .body("{\"from\": 3, \"to\": 2, \"amount\": 12.3}")
            .when()
            .port(TransferTesting.PORT)
            .post("/api/transfer");
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/3")
            .then()
            .assertThat()
            .body("balance", equalTo(87.7f))
            .statusCode(HttpStatus.SC_OK);
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/2")
            .then()
            .assertThat()
            .body("balance", equalTo(24.6f))
            .statusCode(HttpStatus.SC_OK);
    }

    @AfterAll
    public void clean() {
        connect.clean();
    }
}
