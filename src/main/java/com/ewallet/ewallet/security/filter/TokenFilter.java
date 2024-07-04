package com.ewallet.ewallet.security.filter;

import com.ewallet.ewallet.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
       
        filterChain.doFilter(request, response);
        return;
//        var authorizationContent = request.getHeader("Authorization");
//
//
//        if (authorizationContent == null || !authorizationContent.contains("Bearer")) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            logger.info(response.getHeader("Content-Type"));
//            return;
//        }
//
//        var token = authorizationContent.split(" ")[1];
//        if (JwtUtil.verifyToken(token) == null) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            return;
//        }
//
//
//        SecurityContextHolderStrategy contextHolder = SecurityContextHolder.getContextHolderStrategy();
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//
//        Authentication authentication = context.getAuthentication();
////        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken();
//
//        contextHolder.setContext(context);
//
//
//        filterChain.doFilter(request, response);
    }
}
