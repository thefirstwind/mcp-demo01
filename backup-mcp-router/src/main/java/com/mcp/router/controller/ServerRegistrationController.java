package com.mcp.router.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/servers")
public class ServerRegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(ServerRegistrationController.class);
    
    // 使用线程安全的 Map 存储已注册的服务
    private final Map<String, ServerInfo> registeredServers = new ConcurrentHashMap<>();

    /**
     * 服务注册
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerServer(@RequestBody ServerRegistrationRequest request) {
        // 验证请求参数
        if (request == null || request.getServerName() == null || request.getServerName().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid server registration request");
        }

        // 创建服务信息
        ServerInfo serverInfo = new ServerInfo(
            request.getServerName(), 
            request.getHost(), 
            request.getPort(), 
            request.getSupportedTools(),
            request.getMetadata()
        );

        // 注册服务
        registeredServers.put(request.getServerName(), serverInfo);

        // 记录日志
        logger.info("Server registered: {}", request.getServerName());
        logger.info("  - Host: {}", request.getHost());
        logger.info("  - Port: {}", request.getPort());
        logger.info("  - Supported Tools: {}", request.getSupportedTools());

        return ResponseEntity.ok("Server registered successfully");
    }

    /**
     * 服务注销
     */
    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterServer(@RequestParam String serverName) {
        if (serverName == null || serverName.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid server name");
        }

        ServerInfo removedServer = registeredServers.remove(serverName);
        
        if (removedServer != null) {
            logger.info("Server unregistered: {}", serverName);
            return ResponseEntity.ok("Server unregistered successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取所有已注册服务
     */
    @GetMapping("/list")
    public ResponseEntity<List<ServerInfo>> listServers() {
        return ResponseEntity.ok(List.copyOf(registeredServers.values()));
    }

    /**
     * 服务信息类
     */
    public static class ServerInfo {
        private String serverName;
        private String host;
        private int port;
        private List<String> supportedTools;
        private Map<String, Object> metadata;

        public ServerInfo(String serverName, String host, int port, 
                          List<String> supportedTools, Map<String, Object> metadata) {
            this.serverName = serverName;
            this.host = host;
            this.port = port;
            this.supportedTools = supportedTools;
            this.metadata = metadata;
        }

        // Getters and setters
        public String getServerName() { return serverName; }
        public void setServerName(String serverName) { this.serverName = serverName; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public List<String> getSupportedTools() { return supportedTools; }
        public void setSupportedTools(List<String> supportedTools) { this.supportedTools = supportedTools; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 服务注册请求类
     */
    public static class ServerRegistrationRequest {
        private String serverName;
        private String host;
        private int port;
        private List<String> supportedTools;
        private Map<String, Object> metadata;

        // Getters and setters
        public String getServerName() { return serverName; }
        public void setServerName(String serverName) { this.serverName = serverName; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public List<String> getSupportedTools() { return supportedTools; }
        public void setSupportedTools(List<String> supportedTools) { this.supportedTools = supportedTools; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
} 