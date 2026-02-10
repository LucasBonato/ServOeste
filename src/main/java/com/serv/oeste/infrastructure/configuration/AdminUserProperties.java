package com.serv.oeste.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminUserProperties(
        String username,
        String password
) { }
