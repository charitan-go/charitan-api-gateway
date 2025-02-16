package charitan_go.charitan_api_gateway.internal.filter;

import charitan_go.charitan_api_gateway.internal.jwt.JwtService;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    private final List<PathPattern> publicPaths;

    @Autowired
    JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;

        PathPatternParser parser = new PathPatternParser();
        this.publicPaths = List.of(
                // AUTH
                parser.parse("/api/auth/login"),
                parser.parse("/api/auth/register")
        );
    }


    private boolean isPublicPath(String path) {
        PathContainer pathContainer = PathContainer.parsePath(path);
        return publicPaths.stream().anyMatch(pattern -> pattern.matches(pathContainer));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Get path
        String path = exchange.getRequest().getURI().getPath();

        // If is public path
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // Get token
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // For protected path, if token is not available => UNAUTHORIZED
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Claims claims = jwtService.validateAndParseJwt(token.replace("Bearer ", ""));

            // Add claims to headers for ALL downstream services
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject())
                    .header("X-User-Role", claims.get("role", String.class))
//                    .header("X-User-Role", String.join(",", claims.get("roles", List.class)))
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // Execute first
    }

}
