package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.util.StopWatch;

public class TimeSpentTestWatcher extends TestWatcher {

    private final StopWatch stopWatch;

    private long startNano;

    public TimeSpentTestWatcher(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    @Override
    protected void starting(Description description) {
        startNano = System.nanoTime();
        stopWatch.start(description.getMethodName());
    }

    @Override protected void finished(Description description) {
        long nanoElapsed = System.nanoTime() - startNano;
        System.out.printf("Test %s spent %d nanoseconds\n", description.getMethodName(), nanoElapsed);
        stopWatch.stop();
    }
}