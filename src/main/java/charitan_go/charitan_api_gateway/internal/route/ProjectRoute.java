package charitan_go.charitan_api_gateway.internal.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProjectRoute {

    @Bean
    RouteLocator projectRoutes(RouteLocatorBuilder builder) {
        return builder.routes().route("project-server-rest",
                        r -> r.path("/api/project/**")
                .filters(f -> f.stripPrefix(2))
                .uri("lb://project-server-rest"))
                .build();
    }
}
