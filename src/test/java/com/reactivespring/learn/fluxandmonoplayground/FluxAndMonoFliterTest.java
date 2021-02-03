package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFliterTest {

    List<String> names = Arrays.asList("adam","jack","anna","jenny");

    @Test
    public void filterTest(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("a"))
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    public void filterTestLength(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(1)
                .verifyComplete();

    }

}
