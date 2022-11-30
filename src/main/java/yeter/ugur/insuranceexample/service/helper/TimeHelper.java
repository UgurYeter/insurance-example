package yeter.ugur.insuranceexample.service.helper;

import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class TimeHelper {

    private final Clock clock;

    public TimeHelper(Clock clock) {
        this.clock = clock;
    }

    public long getCurrentMilliSecond() {
        return clock.instant().toEpochMilli();
    }
}
