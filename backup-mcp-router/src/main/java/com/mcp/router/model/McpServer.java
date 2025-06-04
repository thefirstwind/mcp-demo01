package com.mcp.router.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpServer {
    private String id;
    private String name;
    private String host;
    private int port;
    private String serviceUrl;
    private List<String> supportedTools;
    private Map<String, Object> metadata;
    private ServerStatus status;
    private double loadFactor;

    public enum ServerStatus {
        HEALTHY, DEGRADED, UNAVAILABLE
    }

    // 根据服务能力和负载评估适用性
    public double calculateSuitability(String requiredTool) {
        if (!supportedTools.contains(requiredTool)) {
            return 0.0;
        }
        
        // 考虑负载因子和状态
        double statusFactor = switch (status) {
            case HEALTHY -> 1.0;
            case DEGRADED -> 0.5;
            case UNAVAILABLE -> 0.0;
        };

        return statusFactor * (1.0 - loadFactor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McpServer mcpServer = (McpServer) o;
        return port == mcpServer.port && 
               Objects.equals(id, mcpServer.id) && 
               Objects.equals(name, mcpServer.name) && 
               Objects.equals(host, mcpServer.host) && 
               Objects.equals(serviceUrl, mcpServer.serviceUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, host, port, serviceUrl);
    }
} 