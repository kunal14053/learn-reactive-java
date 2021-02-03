package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam","jack","anna","jenny");

    @Test
    public void fluxUsingIterable(){

       Flux<String> namesFlux = Flux.fromIterable(names)
               .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","jack","anna","jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray(){

        String[] names = new String[]{"adam","jack","anna","jenny"};

        Flux<String> namesFlux = Flux.fromArray(names)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","jack","anna","jenny")
                .verifyComplete();

    }

    @Test
    public void fluxUsingStream(){

        //steam will pass the element to the flux to one by one as long as we call verify,
        // as stream follow a lazy approach
        Flux<String> namesFlux = Flux.fromStream(names.stream())
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","jack","anna","jenny")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){

       Mono<String> mono =  Mono.justOrEmpty(null); //empty Momo
       //this won't emit any data
       StepVerifier.create(mono.log())
            .verifyComplete();

    }

    @Test
    public void monoUsingSupplier(){

        Supplier<String> stringSupplier = () -> "admin";

        System.out.println(stringSupplier.get());

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMono.log())
                .expectNext("admin")
                .verifyComplete();

    }

    @Test
    public void fluxUsingRange(){

        Flux<Integer> rangeFlux = Flux.range(1,5)
                .log();

        StepVerifier.create(rangeFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }



}
