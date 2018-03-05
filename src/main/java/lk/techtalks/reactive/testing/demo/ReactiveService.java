package lk.techtalks.reactive.testing.demo;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

public class ReactiveService {

    public Flux<Integer> getNumbers(int start, int count) {
        return Flux.range(start, count);
    }

    public Flux<Integer> getNumbers(int start, int count, Function<Integer, Integer> func) {
        return getNumbers(start, count).map(func);
    }

    public Flux<Integer> getNumbers(int start, int count, Duration delay) {
        return getNumbers(start, count).delayElements(delay);
    }

    public Flux<Date> getTimeTicks() {
        return Flux.interval(Duration.ofSeconds(1)).map(tick -> new Date());
    }

}
