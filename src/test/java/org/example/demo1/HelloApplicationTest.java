package org.example.demo1;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HelloApplicationTest {

    @BeforeAll
    static void startJavaFxToolkit() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException ignored) {
            // Another JavaFX test may have initialized the shared toolkit.
        }
    }

    @Test
    void startBuildsAndDisplaysApplicationScene() throws Exception {
        CountDownLatch completed = new CountDownLatch(1);
        AtomicReference<Stage> stageReference = new AtomicReference<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                new HelloApplication().start(stage);
                stageReference.set(stage);
            } catch (Throwable throwable) {
                failure.set(throwable);
            } finally {
                completed.countDown();
            }
        });

        if (!completed.await(10, TimeUnit.SECONDS)) {
            throw new AssertionError("JavaFX application startup timed out");
        }
        if (failure.get() != null) {
            throw new AssertionError("JavaFX application failed to start", failure.get());
        }

        Stage stage = stageReference.get();
        assertNotNull(stage);
        assertEquals("Hello!", stage.getTitle());
        assertNotNull(stage.getScene());

        CountDownLatch closed = new CountDownLatch(1);
        Platform.runLater(() -> {
            stage.close();
            closed.countDown();
        });
        if (!closed.await(10, TimeUnit.SECONDS)) {
            throw new AssertionError("JavaFX application shutdown timed out");
        }
    }
}
