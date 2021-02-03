package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling(){

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.err.println(e);
                    return Flux.just("default1", "default2");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B", "C", "default1", "default2")
                //.expectError(RuntimeException.class)
                //.verify()
                .verifyComplete();

    }


    @Test
    public void fluxErrorHandling_OnErrorReturn(){

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B", "C", "default")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandling_OnErrorMap(){

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                //map from one exception to other/custom exception
                .onErrorMap((e) -> new CustomException(e));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B", "C")
                .expectError(CustomException.class)
                .verify();

    }


    @Test
    public void fluxErrorHandling_OnErrorMap_withRetry(){

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B", "C")
                .expectNext("A","B", "C")
                .expectNext("A","B", "C")
                .expectError(CustomException.class)
                .verify();

    }





}
