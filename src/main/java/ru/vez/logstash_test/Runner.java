package ru.vez.logstash_test;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Random;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static net.logstash.logback.argument.StructuredArguments.value;
import static net.logstash.logback.marker.Markers.appendEntries;

@Slf4j
@AllArgsConstructor
public class Runner implements Runnable{

    private final String name;
    private final int age;

    @Override
    public void run() {

        Instant start = Instant.now();
        randomDelay(1000);
        Duration duration = Duration.between(start, Instant.now());

        /*
         * Add "name":"value" to the JSON output,
         * but only add the value to the formatted message.
         *
         * The formatted message will be `log message value`
         */
        log.info("0. name0={}, age0={}, duration0={} ms", value("name", name), value("age", age), value("duration", duration.toMillis()));

        /*
         * Add "name":"value" to the JSON output,
         * and add name=value to the formatted message.
         *
         * The formatted message will be `log message name=value`
         */
        log.info("1. {}, {}, {} ms", keyValue("name", name), keyValue("age", age), keyValue("duration", duration.toMillis()));

        /*
         * Add "name1":"value1","name2":"value2" to the JSON output by using a map.
         *
         * Note the values can be any object that can be serialized by Jackson's ObjectMapper
         * (e.g. other Maps, JsonNodes, numbers, arrays, etc)
         */
        Map<String, String> myMap = Map.of("name1", "value1", "name2", "value2", "name3", "value3");
        log.info(appendEntries(myMap), "2. log message");
    }

    private void randomDelay(int bound) {
        try {
            Thread.sleep(new Random().nextInt(bound));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted", e);
        }
    }
}
