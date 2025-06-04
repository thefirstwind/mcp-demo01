//package com.mcp.router.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mcp.router.service.InMemoryServiceRegistry;
//import com.mcp.sdk.model.McpServer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/servers")
//public class ServerRegistrationController {
//    private static final Logger logger = LoggerFactory.getLogger(ServerRegistrationController.class);
//
//    @Autowired
//    private InMemoryServiceRegistry serviceRegistry;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    /**
//     * 服务注册
//     */
//    @PostMapping("/register")
//    public ResponseEntity<?> registerServer(@RequestBody Map<String, Object> payload) {
//        try {
//            // 从payload中提取必要的信息，使用更灵活的方式
//            String serverId = extractStringValue(payload, "id", UUID.randomUUID().toString());
//            String serverName = extractStringValue(payload, "serverName", "Unknown Server");
//            String host = extractStringValue(payload, "host", "localhost");
//            Integer port = extractIntValue(payload, "port", 8080);
//
//            // 处理支持的工具列表
//            List<String> supportedTools = extractListValue(payload, "supportedTools", null);
//
//            // 处理元数据
//            Map<String, Object> metadata = extractMapValue(payload, "metadata", new HashMap<>());
//
//            // 处理服务器状态
//            String statusStr = extractStringValue(payload, "status", "STARTING");
//            McpServer.ServerStatus status;
//            try {
//                status = McpServer.ServerStatus.valueOf(statusStr.toUpperCase());
//            } catch (Exception e) {
//                status = McpServer.ServerStatus.STARTING;
//            }
//
//            // 创建服务器实例
//            McpServer server = new McpServer(
//                serverName,
//                host,
//                port,
//                supportedTools != null ? supportedTools.toArray(new String[0]) : new String[0],
//                metadata,
//                status
//            );
//
//            // 设置额外的服务器属性
//            server.setLastHealthCheckTime(Instant.now());
//
//            // 注册服务
//            serviceRegistry.register(server);
//
//            // 记录日志
//            logger.info("Server registered: {}", serverName);
//            logger.info("  - Host: {}", host);
//            logger.info("  - Port: {}", port);
//            logger.info("  - Supported Tools: {}", supportedTools);
//            logger.info("  - Server ID: {}", serverId);
//
//            // 返回成功响应，包含服务器ID
//            return ResponseEntity.ok(Map.of(
//                "message", "Server registered successfully",
//                "serverId", serverId
//            ));
//        } catch (Exception e) {
//            logger.error("Error registering server", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(Map.of(
//                    "error", "Invalid server registration request",
//                    "details", e.getMessage()
//                ));
//        }
//    }
//
//    // 辅助方法：安全地从Map中提取字符串值
//    private String extractStringValue(Map<String, Object> payload, String key, String defaultValue) {
//        Object value = payload.get(key);
//        return value != null ? value.toString() : defaultValue;
//    }
//
//    // 辅助方法：安全地从Map中提取整数值
//    private Integer extractIntValue(Map<String, Object> payload, String key, Integer defaultValue) {
//        Object value = payload.get(key);
//        if (value instanceof Number) {
//            return ((Number) value).intValue();
//        }
//        return defaultValue;
//    }
//
//    // 辅助方法：安全地从Map中提取列表值
//    @SuppressWarnings("unchecked")
//    private List<String> extractListValue(Map<String, Object> payload, String key, List<String> defaultValue) {
//        Object value = payload.get(key);
//        return value instanceof List ? (List<String>) value : defaultValue;
//    }
//
//    // 辅助方法：安全地从Map中提取Map值
//    @SuppressWarnings("unchecked")
//    private Map<String, Object> extractMapValue(Map<String, Object> payload, String key, Map<String, Object> defaultValue) {
//        Object value = payload.get(key);
//        return value instanceof Map ? (Map<String, Object>) value : defaultValue;
//    }
//
//    /**
//     * 服务注销
//     */
//    @PostMapping("/unregister")
//    public ResponseEntity<String> unregisterServer(@RequestParam String serverId) {
//        if (serverId == null || serverId.isEmpty()) {
//            return ResponseEntity.badRequest().body("Invalid server ID");
//        }
//
//        serviceRegistry.unregister(serverId);
//
//        logger.info("Server unregistered: {}", serverId);
//        return ResponseEntity.ok("Server unregistered successfully");
//    }
//
//    /**
//     * 获取所有已注册服务
//     */
//    @GetMapping("/list")
//    public ResponseEntity<List<McpServer>> listServers() {
//        return ResponseEntity.ok(serviceRegistry.getAllServers());
//    }
//}