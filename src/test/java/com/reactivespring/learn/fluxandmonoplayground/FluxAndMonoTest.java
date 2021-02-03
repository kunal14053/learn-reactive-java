package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest(){

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //we can attach error to the flux too
                //if error is not there we will get the onComplete event
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                //once the error occur we won't get ay data
                .concatWith(Flux.just("After Error"))
                //adding log
                .log();

        //When we subscribe, the flux will be going to emit the values to the subscriber
        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println("Exception is: " + e),
                        () -> System.out.println("Completed"));
    }


    @Test
    public void fluxTestElements_WithoutError() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
            .expectNext("Spring")
            .expectNext("Spring Boot")
            .expectNext("Reactive Spring")
            //verify complete is like the subscribe, after which flux will start emitting the events
                // so we need to end the event with verify complete
            .verifyComplete();
    }


    @Test
    public void fluxTestElements_WithError() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                //we can't have both except Error and except Error Message together
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Exception Occurred")
                //in case of error we only have verify
                .verify();
    }

    @Test
    public void fluxTestElementsCount_WithError() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception Occurred")
                .verify();
    }


    @Test
    public void fluxTestElements_WithError1() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                //we can write all in one line too
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .expectErrorMessage("Exception Occurred")
                .verify();
    }

    @Test
    public void monoTest(){

        Mono<String> stringMono =  Mono.just("Spring");
        //we can add method in the created instance too
        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();

    }

    @Test
    public void monoTest_Error(){

        StepVerifier.create(Mono.error(new RuntimeException("Exception occurred")).log())
                .expectError(RuntimeException.class)
                .verify();

    }


}
