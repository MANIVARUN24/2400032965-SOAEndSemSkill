package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * GlobalFilter that runs after the Spring Cloud LoadBalancer has resolved
 * the target service instance. Logs the routed URL and appends diagnostic
 * response headers to demonstrate Round-Robin Load Balancing in action.
 */
@Component
public class RoundRobinLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RoundRobinLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            URI routedUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            if (routedUri != null) {
                log.info("===> [API GATEWAY] Incoming path: {} -> LOAD BALANCED to instance: {} (Port: {})",
                        exchange.getRequest().getURI().getPath(),
                        routedUri,
                        routedUri.getPort());

                exchange.getResponse().getHeaders().add("X-Gateway-Routed-To", routedUri.toString());
                exchange.getResponse().getHeaders().add("X-Gateway-LoadBalancer", "RoundRobinLoadBalancer");
            }
        }));
    }

    @Override
    public int getOrder() {
        // Execute after ReactiveLoadBalancerClientFilter (10150) so the final target instance is known
        return ReactiveLoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER + 10;
    }
}
