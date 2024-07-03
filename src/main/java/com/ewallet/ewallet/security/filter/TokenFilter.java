package com.ewallet.ewallet.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {



        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (IOException | ServletException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,e.getMessage());
        }
    }
}
