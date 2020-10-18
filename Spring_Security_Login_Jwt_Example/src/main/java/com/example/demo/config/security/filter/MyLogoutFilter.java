package com.example.demo.config.security.filter;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyLogoutFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) req, (HttpServletResponse) res, chain);
    }

    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(matchUrl(request)){
            response.reset();
        }else{
            chain.doFilter(request, response);
        }
    }

    private boolean matchUrl(HttpServletRequest request) {
        boolean method = request.getMethod().equals(String.valueOf(HttpMethod.POST));
        boolean path = request.getRequestURI().matches("/logout");
        return method && path;
    }
}