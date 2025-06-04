package com.mcp.remote;

import com.mcp.remote.DefaultMcpServerRegistrar;
import com.mcp.sdk.annotation.McpServerTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mcp.remote"})
public class McpRemoteApplication {
    private static final Logger logger = LoggerFactory.getLogger(McpRemoteApplication.class);
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(McpRemoteApplication.class, args);
    
    }
} 