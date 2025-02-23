package com.example.tomcat.filter.tomcatfilterexample;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**

 */
public class CorsFilter implements Filter {
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,

                         FilterChain filterChain)
    throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            String scheme = request.getScheme();
            String remoteHost = request.getRemoteHost();
            int remotePort = request.getRemotePort();

            String url = String.format("%s://%s:%s", scheme, remoteHost, remotePort);
            httpServletResponse.addHeader("Access-Control-Allow-Origin", url);
            httpServletResponse.addHeader("Access-Control-Allow-Expose-Headers", "*");
            httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST");
            httpServletResponse.addHeader("Access-Control-Allow-Headers", "*");
            httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            if (httpServletRequest.getMethod().equals("OPTIONS")) {
                httpServletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
