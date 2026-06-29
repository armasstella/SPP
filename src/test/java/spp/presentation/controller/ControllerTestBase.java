package spp.presentation.controller;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ControllerTestBase {

    private static final AtomicBoolean javafxStarted = new AtomicBoolean(false);

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        if (javafxStarted.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> latch.countDown());
            latch.await();
        }
    }
}