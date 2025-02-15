package charitan_go.charitan_api_gateway.internal.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProfileRoute {

    @Bean
    RouteLocator profileRoutes(RouteLocatorBuilder builder) {
        return builder.routes().route("profile-server-rest",
                        r -> r.path("/api/profile/**")
                .filters(f -> f.stripPrefix(2))
                .uri("lb://profile-server-rest"))
                .build();
    }
}
