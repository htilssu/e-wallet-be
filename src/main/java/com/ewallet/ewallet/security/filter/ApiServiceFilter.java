package com.ewallet.ewallet.security.filter;

import com.ewallet.ewallet.link_service.Service;
import com.ewallet.ewallet.link_service.ServiceRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class ApiServiceFilter implements Filter {

    ServiceRepository serviceRepository;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws
                                            IOException,
                                            ServletException {
        var req = (HttpServletRequest) request;
        var apiKey = req.getHeader("X-Api");


        if (apiKey == null || apiKey.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        ;

        Service service = serviceRepository.findServiceByApiKey(apiKey).block();
        if (service == null) return;
    }
}
