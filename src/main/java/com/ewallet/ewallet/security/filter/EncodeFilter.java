package com.ewallet.ewallet.security.filter;

import jakarta.servlet.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EncodeFilter implements Filter, WebFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        response.setContentType("json/application; charset=utf-8");
        chain.doFilter(request, response);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().getHeaders().set("Content-Type", "application/json; charset=utf-8");
        return chain.filter(exchange);
    }
}

