package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {

        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(50))
                .log();
        longFlux.subscribe((x) -> System.out.println(x));

        Thread.sleep(1000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {

        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(50))
                //just take value and will emit only that much value
                //so its a finite flux
                .take(3)
                .log();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();

    }

    @Test
    public void infiniteSequenceMap() throws InterruptedException {

        Flux<Integer> longFlux = Flux.interval(Duration.ofMillis(100))
                //adding a delay of 1sec after every value is emitted
                .delayElements(Duration.ofSeconds(1))
                .map(l -> new Integer(l.intValue()))
                .take(3)
                .log();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();

    }

}
