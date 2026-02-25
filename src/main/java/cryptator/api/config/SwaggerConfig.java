/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI Configuration
 * Provides interactive API documentation at /swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI cryptatorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cryptator API")
                        .description("REST API for solving and generating cryptarithms (alphametic puzzles)\n\n" +
                                "A cryptarithm is a mathematical puzzle where digits are replaced by letters. " +
                                "For example: SEND + MORE = MONEY\n\n" +
                                "**Features:**\n" +
                                "- Solve cryptarithms with various solver types\n" +
                                "- Generate cryptarithms from word lists\n" +
                                "- Cancel long-running tasks")
                        .version("1.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Arnaud Malapert")
                                .url("http://www.i3s.unice.fr/~malapert/"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://spdx.org/licenses/MIT.html")));
    }
}
