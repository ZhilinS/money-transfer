package com.zhilin;

import com.google.gson.Gson;
import com.zhilin.db.Connect;
import com.zhilin.db.Session;
import com.zhilin.http.Router;
import com.zhilin.http.req.ReqDeposit;
import com.zhilin.http.req.ReqTransfer;
import com.zhilin.http.req.ReqWithdraw;
import com.zhilin.http.routes.Accounts;
import com.zhilin.http.routes.Transfers;
import com.zhilin.job.Locker;
import com.zhilin.job.LockerWrap;
import com.zhilin.job.Reactor;
import com.zhilin.query.account.AccountCreate;
import com.zhilin.query.account.AccountOf;
import com.zhilin.query.account.AccountUpdate;
import com.zhilin.query.account.AccountsOf;
import com.zhilin.query.transfer.TransferCreate;
import com.zhilin.query.transfer.TransferOf;
import com.zhilin.query.transfer.TransferUpdate;
import com.zhilin.query.transfer.TransfersOf;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferTesting {

    private final static int PORT = 8081;

    private Connect connect;

    @BeforeAll
    public void setup() {
        connect = new Connect("jdbc:sqlite:memory:test", 1);
        final Session session = connect.session();
        new Router(
            TransferTesting.PORT,
            new Accounts(
                new AccountOf(session),
                new AccountsOf(session),
                new AccountUpdate(session),
                new AccountCreate(session),
                new Reactor(
                    new TransferCreate(session),
                    new TransferUpdate(session),
                    new LockerWrap(
                        new Locker()
                    )
                )
            ),
            new Transfers(
                new TransferOf(session),
                new TransfersOf(session)
            )
        ).init();
    }

    @BeforeEach
    public void testPostAccount() throws IOException {
        connect.init();
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

    @AfterEach
    public void clean() {
        connect.clean();
    }

    @Test
    @Order(1)
    public void testGetAccountsList() {
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/account")
            .then()
            .assertThat()
            .body("size()", equalTo(10))
            .statusCode(HttpStatus.SC_OK);
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

    @Test
    @Order(3)
    public void testWithdraw() {
        this.withdraw(3, 12.3f);
        this.balance(3, 87.7f);
    }

    @Test
    @Order(4)
    public void testDeposit() {
        this.deposit(3, 12.3f);
        this.balance(3, 112.3F);
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
                    () -> this.transfer(3, 2, 1)
                )
            ));
        IntStream.rangeClosed(100, 199)
            .boxed()
            .forEach(i -> tasks.add(
                new Thread(
                    () -> this.transfer(2, 3, 1)
                )
            ));
        Collections.shuffle(tasks);
        final List<? extends Future<?>> futures = tasks.stream()
            .map(pool::submit)
            .collect(Collectors.toList());
        for (Future future : futures) {
            future.get();
        }
        this.balance(3, 100.0f);
        this.balance(2, 112.3f);
    }

    @Test
    @Order(6)
    public void testTransactionsLog() {
        this.withdraw(4, 32.1f);
        this.deposit(1, 413.98f);
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/log/4")
            .then()
            .assertThat()
            .body("type", equalTo("WITHDRAW"))
            .body("status", equalTo("COMPLETED"))
            .body("first", equalTo(4))
            .statusCode(HttpStatus.SC_OK);
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/log/1")
            .then()
            .assertThat()
            .body("type", equalTo("DEPOSIT"))
            .body("status", equalTo("COMPLETED"))
            .body("second", equalTo(1))
            .statusCode(HttpStatus.SC_OK);
        given()
            .when()
            .port(TransferTesting.PORT)
            .get("/api/log")
            .then()
            .assertThat()
            .body("size()", is(2))
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(7)
    public void shouldNotDropBalanceBelowZero() {
        this.withdraw(2, 120);
    }

    private void withdraw(
        final int account,
        final float amount
    ) {
        given()
            .body(
                new Gson().toJson(
                    new ReqWithdraw(account, amount)
                )
            )
            .when()
            .port(TransferTesting.PORT)
            .post("/api/account/withdraw");
    }

    private void deposit(
        final int account,
        final float amount
    ) {
        given()
            .body(
                new Gson().toJson(
                    new ReqDeposit(account, amount)
                )
            )
            .when()
            .port(TransferTesting.PORT)
            .post("/api/account/deposit");
    }

    private void transfer(
        final int from,
        final int to,
        final float amount
    ) {
        given()
            .body(
                new Gson().toJson(
                    new ReqTransfer(from, to, amount)
                )
            )
            .when()
            .port(TransferTesting.PORT)
            .post("/api/transfer");
    }

    private void balance(
        final int account,
        final float balance
    ) {
        given()
            .when()
            .port(TransferTesting.PORT)
            .get(
                String.format(
                    "/api/account/%d",
                    account
                )
            )
            .then()
            .assertThat()
            .body("balance", equalTo(balance))
            .statusCode(HttpStatus.SC_OK);
    }
}
