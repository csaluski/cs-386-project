package edu.nau.cs386.test.integration;


import edu.nau.cs386.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Integration Tests for Pulp
 */
public class IntegrationTest {

    private void testConnection(String requestURI, HttpMethod httpMethod) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        VertxTestContext testContext = new VertxTestContext();

        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
            HttpClient client = vertx.createHttpClient();
            client.request(httpMethod, 8888, "localhost", "/")
                .flatMap(req -> req.send().compose(HttpClientResponse::body))
                .onFailure(testContext::failNow)
                .onSuccess(buffer -> testContext.verify(testContext::completeNow));
        }));

        Assertions.assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
        closeVertx(vertx);
    }

    private void closeVertx(Vertx vertx) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        vertx.close(ar -> latch.countDown());
        Assertions.assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Start the MainVerticle, test the index page.")
    void pulpTestIndex() throws InterruptedException {
        testConnection("/", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the login page.")
    void pulpTestLogin() throws InterruptedException {
        testConnection("/login", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the logout page.")
    void pulpTestLogout() throws InterruptedException {
        testConnection("/logout", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the edit page.")
    void pulpTestEdit() throws InterruptedException {
        testConnection("/edit", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the create page.")
    void pulpTestCreate() throws InterruptedException {
        testConnection("/create", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the tag page.")
    void pulpTestTag() throws InterruptedException {
        testConnection("/tag", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the remove tag page.")
    void pulpTestRemoveTag() throws InterruptedException {
        testConnection("/removeTag", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the profile page.")
    void pulpTestProfile() throws InterruptedException {
        testConnection("/profile", HttpMethod.GET);
    }

    @Test
    @DisplayName("Start the MainVerticle, test the browse paper page.")
    void pulpTestBrowsePaper() throws InterruptedException {
        testConnection("/browsePaper", HttpMethod.GET);
    }

}
