package ru.javawebinar.topjava;

import org.junit.rules.ExternalResource;
import org.springframework.util.StopWatch;

public class TimeSpentSummary extends ExternalResource {

    private final StopWatch stopWatch;

    public TimeSpentSummary(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    @Override
    protected void after() {
        System.out.println(stopWatch.prettyPrint());
    }
}
