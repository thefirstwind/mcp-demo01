package com.mcp.remote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mcp.sdk.model.McpServer;
import com.mcp.sdk.processor.McpServerToolProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mcp.remote", "com.mcp.sdk"})
public class McpRemoteApplication {
    private static final Logger logger = LoggerFactory.getLogger(McpRemoteApplication.class);

    @Resource
    private static McpServerToolProcessor mcpServerToolProcessor;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 配置 ObjectMapper 以改善 JSON 处理
//        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
//            .serializationInclusion(JsonInclude.Include.NON_NULL)
//            .featuresToDisable(
//                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
//                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
//            )
//            .build();
//
//        // 自定义消息转换器
//        restTemplate.getMessageConverters().forEach(converter -> {
//            if (converter instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) {
//                ((org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) converter)
//                    .setObjectMapper(objectMapper);
//            }
//        });
//
//        return restTemplate;
//    }
//
//    @Bean
//    public Runnable registerServerWithRouter(RestTemplate restTemplate) {
//        return () -> {
//            try {
//                String serverName = "MCP-REMOTE";
//                String host = InetAddress.getLocalHost().getHostAddress();
//                int port = 8081; // 确保与application.yml中的端口一致
//
//                // 准备服务器元数据
//                Map<String, Object> metadata = new HashMap<>();
//                metadata.put("java.version", System.getProperty("java.version"));
//                metadata.put("os.name", System.getProperty("os.name"));
//                metadata.put("os.arch", System.getProperty("os.arch"));
//
//                // 支持的工具列表
//                List<String> supportedTools = Arrays.asList("remoteTool1", "remoteTool2");
//
//                // 创建注册请求对象
////                Map<String, Object> registrationPayload = new HashMap<>();
////                registrationPayload.put("serverName", serverName);
////                registrationPayload.put("host", host);
////                registrationPayload.put("port", port);
////                registrationPayload.put("supportedTools", supportedTools);
////                registrationPayload.put("metadata", metadata);
//
//                McpServer mcpServer = new McpServer(
//                        serverName,
//                        host,
//                        port,
//                        supportedTools.toArray(new String[0]),
//                        metadata,
//                        McpServer.ServerStatus.STARTING
//                );
//
//                // 发送注册请求到路由器
//                String registrationUrl = "http://localhost:8761/router/register";
//                restTemplate.postForObject(registrationUrl, mcpServer, McpServer.class);
//                logger.info("Server registered successfully with MCP router");
//            } catch (Exception e) {
//                logger.error("Failed to register server with MCP router", e);
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(McpRemoteApplication.class, args);
    }
} 