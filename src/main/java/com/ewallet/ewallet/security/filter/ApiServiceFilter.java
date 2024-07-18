package com.ewallet.ewallet.security.filter;

import com.ewallet.ewallet.partner.Partner;
import com.ewallet.ewallet.partner.PartnerRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class ApiServiceFilter implements Filter {

    PartnerRepository partnerRepository;

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


        Partner partner = null;
        try {
            partner = partnerRepository.getPartnerByApiKey(apiKey).block();
        } catch (RuntimeException r) {
            System.out.println("Error: " + r.getMessage());
        }

        if (partner != null) {
            var context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();

            if (authentication == null) {
                context.setAuthentication(new UsernamePasswordAuthenticationToken(partner
                        , apiKey, Collections.singleton(new SimpleGrantedAuthority("PARTNER"))));
            }

            req.setAttribute("partner", partner);
        }

        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        res.getWriter().println("{\"message\": \"API key không hợp lệ\"}");
        res.setContentType("application/json");
    }
}
