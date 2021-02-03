package com.reactivespring.learn.handler;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
//@WebFluxTest this won't work here as it won't scan @Component
@SpringBootTest //won't create webTestClient
@AutoConfigureWebTestClient //need to use this to create a bean for WebTestClient
public class SampleHandlerFunctionTest {

    @Autowired
    //this is a non blocking client
    WebTestClient webTestClient;

    @Test
    public void flux_approach1(){

        Flux<Integer> integerFlux = webTestClient.get()
                .uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()//make the call to the endpoint
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1,2,3,4)
                .verifyComplete();

    }

    @Test
    public void mono(){

        Integer expectedValue = new Integer(1);

        webTestClient.get()
                .uri("/functional/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((result) -> {
                    assertEquals(expectedValue, result.getResponseBody());
                });


    }


}
