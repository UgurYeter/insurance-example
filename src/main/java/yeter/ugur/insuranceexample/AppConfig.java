package yeter.ugur.insuranceexample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AppConfig {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
