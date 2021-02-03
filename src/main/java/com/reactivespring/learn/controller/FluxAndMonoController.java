package com.reactivespring.learn.controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux(){
        //here the browser is like the subscriber of the flux
        //we introduced the delay but the browser is ab locking client
        // so it will wait till all the data is not returned,
        // the browser return time is json, so it only worry about that
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFluxStream(){
        //here the result will be rendered as the stream
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/infinitefluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnInfiniteFluxStream(){
        //here the result will be rendered as the stream
        //when we shut the server it will send the cancel event and the client will handle it
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/infinitefluxstreamMilli", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnInfiniteFluxStream1(){
        //here the result will be rendered as the stream
        return Flux.interval(Duration.ofMillis(10))
                .log();
    }


    @GetMapping("/mono")
    public Mono<Integer> returnMono(){

        return Mono.just(1)
                .log();
    }



}
