package com.ewallet.ewallet.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {



        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (IOException | ServletException e) {
            System.out.println(e.getMessage());
        }
    }
}
