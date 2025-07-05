package com.lift.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CapacityConfig {
    // Example: future dynamic configurations
    public int getDefaultCapacity() {
        return 8;
    }
}
