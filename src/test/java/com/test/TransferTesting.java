package com.test;

import com.google.gson.Gson;
import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.http.routes.Accounts;
import com.test.job.Reactor;
import com.test.query.transfer.TransferUpdate;
import com.test.query.transfer.TransferCreate;
import com.test.query.account.AccountCreate;
import com.test.query.account.AccountOf;
import com.test.query.account.AccountUpdate;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferTesting {

    private final static int PORT = 8081;

    private Connect connect;

    @BeforeAll
    public void setup() {
        connect = new Connect("jdbc:sqlite:memory:test", 1);
        connect.init();
        final Session session = connect.session();
        new Router(
            TransferTesting.PORT,
            new Accounts(
                new AccountOf(session),
                new AccountUpdate(session),
                new AccountCreate(session),
                new Reactor(
                    new TransferCreate(session),
                    new TransferUpdate(session)
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
    public void testTransfer() throws InterruptedException, ExecutionException {
        final ExecutorService pool = Executors.newFixedThreadPool(10);
        final List<Runnable> tasks = new ArrayList<>(200);
        IntStream.rangeClosed(0, 99)
            .boxed()
            .forEach(i -> tasks.add(
                new Thread(
                    () -> given()
                        .body("{\"from\": 3, \"to\": 2, \"amount\": 1}")
                        .when()
                        .port(TransferTesting.PORT)
                        .post("/api/transfer")
                )
            ));
        IntStream.rangeClosed(100, 199)
            .boxed()
            .forEach(i -> tasks.add(
                new Thread(
                    () -> given()
                        .body("{\"from\": 2, \"to\": 3, \"amount\": 1}")
                        .when()
                        .port(TransferTesting.PORT)
                        .post("/api/transfer")
                )
            ));
        Collections.shuffle(tasks);
        final List<? extends Future<?>> futures = tasks.stream()
            .map(pool::submit)
            .collect(Collectors.toList());
        for (Future future : futures) {
            future.get();
        }
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/3")
            .then()
            .assertThat()
            .body("balance", equalTo(100.0f))
            .statusCode(HttpStatus.SC_OK);
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account/2")
            .then()
            .assertThat()
            .body("balance", equalTo(112.3f))
            .statusCode(HttpStatus.SC_OK);
    }

    @AfterAll
    public void clean() {
        connect.clean();
    }
}
