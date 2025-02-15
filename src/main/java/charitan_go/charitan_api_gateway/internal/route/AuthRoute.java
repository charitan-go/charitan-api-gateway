package charitan_go.charitan_api_gateway.internal.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuthRoute {

    @Bean
    RouteLocator authRoutes(RouteLocatorBuilder builder) {
        return builder.routes().route("auth-server-rest",
                        r -> r.path("/api/auth/**")
                                .filters(f -> f.stripPrefix(2))
                                .uri("lb://auth-server-rest"))
                .build();
    }
}
