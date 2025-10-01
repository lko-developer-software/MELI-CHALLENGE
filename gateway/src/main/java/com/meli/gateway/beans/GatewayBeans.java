package com.meli.gateway.beans;

import java.util.Set;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.meli.gateway.filters.AuthFilter;


@Configuration
public class GatewayBeans {

    @Autowired
    private AuthFilter authFilter;
 
    @Bean
    @Profile(value = "dinamic-routes-cb")
    public RouteLocator routeLocatorEurekaOnCB(RouteLocatorBuilder builder) {
        return builder
                .routes()
                // Ruta con un solo path parameter
                .route(route -> route
                        .path("/product-detail/detail/**")
                        .filters(filter -> {
                            filter.circuitBreaker(config -> config
                                    .setName("gateway-cb-single")
                                    .setStatusCodes(Set.of("500", "400"))
                                    .setFallbackUri("forward:/product-detail-fallback/detail"));
                            return filter;
                        })
                        .uri("lb://product-detail")
                )
                // Rutas de fallback correspondientes
                .route(route -> route
                        .path("/product-detail-fallback/detail/**")
                        .uri("lb://product-detail-fallback")
                )
                .build();
    }

     @Bean
    @Profile(value = "meli-auth")
    public RouteLocator routeLocatorOauth2(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(route -> route
                        .path("/product-detail/detail/**")
                        .filters(filter -> {
                            filter.circuitBreaker(config -> config
                                    .setName("gateway-cb")
                                    .setStatusCodes(Set.of("500", "400"))
                                    .setFallbackUri("forward:/product-detail-fallback/detail"));
                            filter.filter(this.authFilter);
                            return filter;
                        })
                        .uri("lb://product-detail")
                )
                .route(route -> route
                        .path("/product-detail-fallback/detail/**")
                        .filters(filter -> filter.filter(this.authFilter))
                        .uri("lb://product-detail-fallback")
                )
                .route(route -> route
                        .path("/auth-server/auth/**")
                        .uri("lb://auth-server")
                )
                .build();
    }

}
