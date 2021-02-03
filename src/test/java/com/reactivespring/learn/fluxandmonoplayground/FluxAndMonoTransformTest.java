package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam","jack","anna","jenny");

    @Test
    public void transformUsingMap(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Length(){

        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(4,4,4,5)
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Length_Repeat(){

        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .repeat(1)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(4, 4, 4, 5, 4, 4, 4,5)
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Filter(){

        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length()>4)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("JENNY")
                .verifyComplete();

    }


    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"newValue");
    }

    @Test
    public void transformUsingFlatMap(){

        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })//make a db/external service call that return a flux
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap_usingParallel(){

        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) // going to pass as Flux<Flux<String>> : (A,B) (C,D) (E,F)
                .flatMap((s) ->
                    s.map(this::convertToList).subscribeOn(parallel())
                            .flatMap(x -> Flux.fromIterable(x)))
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap_usingParallel_maintainOrder(){

        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) // going to pass as Flux<Flux<String>> : (A,B) (C,D) (E,F)
                /*.concatMap((s) ->
                        s.map(this::convertToList).subscribeOn(parallel())
                                .flatMap(x -> Flux.fromIterable(x)))*/
                .flatMapSequential((s) ->
                        s.map(this::convertToList).subscribeOn(parallel())
                                .flatMap(x -> Flux.fromIterable(x)))
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(12)
                .verifyComplete();

    }



}
