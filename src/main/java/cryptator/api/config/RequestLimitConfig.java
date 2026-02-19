/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.config;

import cryptator.api.filter.RequestLimitFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for limiting concurrent requests to the API
 */
@Configuration
public class RequestLimitConfig {

    @Value("${api.request.max-concurrent:100}")
    private int maxConcurrentRequests;

    @Bean
    public FilterRegistrationBean<RequestLimitFilter> requestLimitFilter() {
        FilterRegistrationBean<RequestLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLimitFilter(maxConcurrentRequests));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
