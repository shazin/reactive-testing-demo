package lk.techtalks.reactive.testing.demo;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ReactiveServiceTest {

    private ReactiveService reactiveService = new ReactiveService();

    @Test
    public void getNumbers() {
        StepVerifier.create(reactiveService.getNumbers(0, 10).doOnNext(System.out::println))
                .expectSubscription()
                .expectNext(0)
                .expectNextCount(4)
                .expectNext(5, 6, 7)
                .expectNext(8)
                .expectNext(9)
                .verifyComplete();
    }

    @Test
    public void getNumbers_Division() {
        StepVerifier.create(Flux.zip(reactiveService.getNumbers(1, 10, (i) -> i / 0)
                                .checkpoint("division", true) // division checkpoint
                                .doOnError(t -> t.printStackTrace())
                                .doOnNext(System.out::println),
                            reactiveService.getNumbers(10, 10)
                                .checkpoint("numbers", true), (n1, n2) -> n1 + n2))
                .expectSubscription()
                .expectError(ArithmeticException.class)
                .verify();
    }

    @Test
    public void getNumbers_Substract() {
        StepVerifier.create(reactiveService.getNumbers(0, 10, (i) -> i - 10).doOnNext(System.out::println))
                .expectSubscription()
                .expectNext(-10)
                .expectNext(-9)
                .expectNext(-8, -7, -6, -5, -4)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getNumbers_DelayElements() {
        StepVerifier.withVirtualTime(() -> reactiveService.getNumbers(0, 10, Duration.ofSeconds(1)).doOnNext(System.out::println))
                .expectSubscription()
                .expectNext(0)
                .thenAwait(Duration.ofSeconds(1))
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .verifyComplete();
    }

    @Test
    public void getNumbersAndgetTimeTicks() {
        StepVerifier.create(Flux.zip(reactiveService.getNumbers(0, 10, Duration.ofSeconds(1)),
                                     reactiveService.getTimeTicks(),
                                    (no, tick) -> no + ". " +tick)
                                .doOnNext(System.out::println))
                .expectSubscription()
                .expectNextCount(10)
                .verifyComplete();
    }
}
