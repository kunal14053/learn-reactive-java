package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge(){

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> fluxMerge = Flux.merge(flux1,flux2);

        StepVerifier.create(fluxMerge.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay(){

        Flux<String> flux1 = Flux.just("A", "B", "C")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        Flux<String> fluxMerge = Flux.merge(flux1,flux2);

        StepVerifier.create(fluxMerge.log())
                .expectSubscription()
                .expectNextCount(6)
                //.expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay(){


        Flux<String> flux1 = Flux.just("A", "B", "C")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F")
                .delayElements(Duration.ofSeconds(1));
        //concat takes more time but maintain the order
        Flux<String> fluxMerge = Flux.concat(flux1,flux2);

        StepVerifier.create(fluxMerge.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay_withVirtualTime(){

        VirtualTimeScheduler.getOrSet();

        Flux<String> flux1 = Flux.just("A", "B", "C")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        Flux<String> fluxMerge = Flux.concat(flux1,flux2);

        StepVerifier.withVirtualTime(() ->fluxMerge.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip(){

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> fluxMerge = Flux.zip(flux1,flux2, (t1,t2) -> {
            return t1.concat(t2);
        });

        StepVerifier.create(fluxMerge.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

}
