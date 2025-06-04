package com.mcp.servertwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mcp.servertwo", "com.mcp.sdk"})
public class McpServerTwoApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerTwoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Runnable registerServerWithRouter(RestTemplate restTemplate) {
        return () -> {
            try {
                String serverName = "McpServerTwo";
                String host = InetAddress.getLocalHost().getHostAddress();
                int port = 18082; // 确保与application.yml中的端口一致

                // 准备服务器元数据
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("java.version", System.getProperty("java.version"));
                metadata.put("os.name", System.getProperty("os.name"));
                metadata.put("os.arch", System.getProperty("os.arch"));

                // 支持的工具列表
                List<String> supportedTools = Arrays.asList("serverTwoTool1", "serverTwoTool2");

                // 创建注册请求对象
                Map<String, Object> registrationPayload = new HashMap<>();
                registrationPayload.put("serverName", serverName);
                registrationPayload.put("host", host);
                registrationPayload.put("port", port);
                registrationPayload.put("supportedTools", supportedTools);
                registrationPayload.put("metadata", metadata);

                // 发送注册请求到路由器
                String registrationUrl = "http://localhost:8761/servers/register"; // 更新路径
                restTemplate.postForObject(registrationUrl, registrationPayload, Void.class);
                System.out.println("Server registered successfully with MCP router");
            } catch (Exception e) {
                System.err.println("Failed to register server with MCP router: " + e.getMessage());
                e.printStackTrace(); // 打印完整的异常堆栈
            }
        };
    }
} 