package com.mcp.sdk.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class McpServer implements Serializable {
    public enum ServerStatus {
        HEALTHY, DEGRADED, UNAVAILABLE, STARTING, SHUTTING_DOWN
    }

    private Map<String,Object> metadata;
    private String id;
    private String host;
    private int port;
    private String[] supportedTools;
    private ServerStatus status;
    private Instant lastHealthCheckTime;
    private Instant registrationTime;
    private Instant lastStatusChangeTime;
    private double loadFactor; // 0.0 to 1.0
    private int consecutiveFailedChecks;
    private String lastErrorMessage;
    private String serverName;

    // 单一的 JsonCreator 构造函数，支持所有可能的参数
    @JsonCreator
    public McpServer(
        @JsonProperty("serverName") String serverName,
        @JsonProperty("host") String host, 
        @JsonProperty("port") int port, 
        @JsonProperty("supportedTools") String[] supportedTools,
        @JsonProperty("metadata") Map<String,Object> metadata,
        @JsonProperty("status") ServerStatus status
    ) {
        // 使用提供的值或默认值
        this.id = UUID.randomUUID().toString();
        this.serverName = serverName;
        this.host = (host != null && !host.isEmpty()) ? host : "localhost";
        this.port = (port > 0) ? port : 8080;
        this.supportedTools = (supportedTools != null) ? supportedTools : new String[0];
        this.metadata = (metadata != null) ? metadata : new HashMap<>();
        this.status = (status != null) ? status : ServerStatus.STARTING;
        
        // 初始化时间相关字段
        this.registrationTime = Instant.now();
        this.lastStatusChangeTime = Instant.now();
        this.loadFactor = 0.0;
        this.consecutiveFailedChecks = 0;
    }


    // 获取基础URL
    public String getBaseUrl() {
        return String.format("http://%s:%d", host, port);
    }

    // 判断服务器是否应该被移除
    public boolean shouldBeRemoved() {
        // 如果连续失败检查次数超过3次，且状态为不可用
        return consecutiveFailedChecks > 3 && 
               (status == ServerStatus.UNAVAILABLE || status == ServerStatus.SHUTTING_DOWN);
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; } // 添加setter

    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String[] getSupportedTools() { return supportedTools; }
    public void setSupportedTools(String[] supportedTools) { this.supportedTools = supportedTools; }

    public ServerStatus getStatus() { return status; }
    public void setStatus(ServerStatus status) {
        if (this.status != status) {
            this.status = status;
            this.lastStatusChangeTime = Instant.now();
            
            // 重置或增加失败检查计数器
            if (status == ServerStatus.HEALTHY) {
                this.consecutiveFailedChecks = 0;
            } else if (status == ServerStatus.UNAVAILABLE) {
                this.consecutiveFailedChecks++;
            }
        }
    }

    public Map<String,Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String,Object> metadata) { this.metadata = metadata; }

    public Instant getLastHealthCheckTime() { return lastHealthCheckTime; }
    public void setLastHealthCheckTime(Instant lastHealthCheckTime) { 
        this.lastHealthCheckTime = lastHealthCheckTime; 
    }

    public Instant getRegistrationTime() { return registrationTime; }
    public Instant getLastStatusChangeTime() { return lastStatusChangeTime; }

    public double getLoadFactor() { return loadFactor; }
    public void setLoadFactor(double loadFactor) { this.loadFactor = loadFactor; }

    @Override
    public String toString() {
        return "McpServer{" +
               "id='" + id + '\'' +
               ", serverName='" + serverName + '\'' +
               ", host='" + host + '\'' +
               ", port=" + port +
               ", status=" + status +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McpServer mcpServer = (McpServer) o;
        return port == mcpServer.port && 
               Objects.equals(id, mcpServer.id) && 
               Objects.equals(serverName, mcpServer.serverName) && 
               Objects.equals(host, mcpServer.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverName, host, port);
    }
} 