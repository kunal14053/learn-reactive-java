package com.reactivespring.learn.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest(){
        Flux<Integer> finiteFlux = Flux.range(1,10).log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenRequest(1)
                .expectNext(3)
                //if we cancel we won't receive the onComplete Event
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure(){

        Flux<Integer> finiteFlux = Flux.range(1,10).log();

        finiteFlux.subscribe((element) -> System.out.println(element),
                (ex) -> System.err.println(ex),
                () -> System.out.println("Done"),
                (subscription) -> subscription.request(2));

    }

    @Test
    public void backPressure_Cancel(){

        Flux<Integer> finiteFlux = Flux.range(1,10).log();

        finiteFlux.subscribe((element) -> System.out.println(element),
                (ex) -> System.err.println(ex),
                () -> System.out.println("Done"),
                (subscription) -> subscription.cancel());

    }


    @Test
    public void customizedBackPressure(){

        Flux<Integer> finiteFlux = Flux.range(1,10).log();
        //hook on value to control on the basics of that
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println(value);
                if(value == 4){
                    cancel();
                }
            }
        });

    }

}
