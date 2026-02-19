/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * Filter to limit the number of concurrent requests to the API
 * Uses a Semaphore to control access and returns 429 (Too Many Requests) when limit is exceeded
 */
public class RequestLimitFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLimitFilter.class);
    
    private final Semaphore semaphore;
    private final int maxConcurrentRequests;

    public RequestLimitFilter(int maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.semaphore = new Semaphore(maxConcurrentRequests, true);
        logger.info("RequestLimitFilter initialized with max concurrent requests: {}", maxConcurrentRequests);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        boolean permitAcquired = semaphore.tryAcquire();
        
        if (!permitAcquired) {
            logger.warn("Request rejected: maximum concurrent requests limit ({}) reached", maxConcurrentRequests);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(
                String.format("{\"error\":\"Too many concurrent requests\",\"message\":\"Server is currently handling maximum allowed concurrent requests (%d). Please try again later.\"}", 
                    maxConcurrentRequests)
            );
            return;
        }

        try {
            logger.debug("Request accepted. Available permits: {}", semaphore.availablePermits());
            chain.doFilter(request, response);
        } finally {
            semaphore.release();
            logger.debug("Request completed. Available permits: {}", semaphore.availablePermits());
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }

    /**
     * Get the number of available permits
     * @return number of available permits
     */
    public int getAvailablePermits() {
        return semaphore.availablePermits();
    }

    /**
     * Get the maximum number of concurrent requests allowed
     * @return maximum concurrent requests
     */
    public int getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }
}
