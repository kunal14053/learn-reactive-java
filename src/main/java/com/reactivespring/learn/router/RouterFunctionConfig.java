package com.reactivespring.learn.router;

import com.reactivespring.learn.handler.SampleHadlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
//map incoming request to appropriate handler
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> route(SampleHadlerFunction hadlerFunction){
        //the router function picks up the mapping and
        // call the appropriate handler function associated with it,
        // similar to @GetMapping
        return RouterFunctions
                .route(GET("/functional/flux")
                        .and(accept(MediaType.APPLICATION_JSON)),
                        hadlerFunction::flux)
                //this was we can add other mapping too
                .andRoute(GET("/functional/mono")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        hadlerFunction::mono);
    }

}
