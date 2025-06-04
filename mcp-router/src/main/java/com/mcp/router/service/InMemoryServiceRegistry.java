package com.mcp.router.service;

import com.mcp.sdk.model.McpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class InMemoryServiceRegistry implements McpServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryServiceRegistry.class);
    private final ConcurrentHashMap<String, McpServer> servers = new ConcurrentHashMap<>();
    private final ExecutorService healthCheckExecutor = Executors.newFixedThreadPool(5);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void register(McpServer server) {
        servers.put(server.getId(), server);
        performHealthCheck(server);
        logger.info("Server registered: {}", server);
    }

    @Override
    public void unregister(String serverId) {
        McpServer server = servers.remove(serverId);
        if (server != null) {
            logger.info("Server unregistered: {}", server);
        }
    }

    @Override
    public List<McpServer> getAllServers() {
        return new ArrayList<>(servers.values());
    }

    @Override
    public Optional<McpServer> findServerById(String serverId) {
        return Optional.ofNullable(servers.get(serverId));
    }

    @Override
    public List<McpServer> findServersByTool(String tool) {
        return servers.values().stream()
                .filter(server -> containsTool(server.getSupportedTools(), tool))
                .collect(Collectors.toList());
    }

    @Override
    public McpServer selectBestServer(String tool) {
        List<McpServer> compatibleServers = findServersByTool(tool).stream()
                .filter(server -> server.getStatus() == McpServer.ServerStatus.HEALTHY)
                .sorted((s1, s2) -> {
                    // 优先级：健康状态 > 负载因子
                    int statusComparison = s1.getStatus().compareTo(s2.getStatus());
                    if (statusComparison != 0) return statusComparison;
                    return Double.compare(s1.getLoadFactor(), s2.getLoadFactor());
                })
                .collect(Collectors.toList());

        return compatibleServers.isEmpty() ? null : compatibleServers.get(0);
    }

    private boolean containsTool(String[] tools, String tool) {
        if (tools == null) return false;
        for (String t : tools) {
            if (t.equalsIgnoreCase(tool)) return true;
        }
        return false;
    }

    private void performHealthCheck(McpServer server) {
//        logger.info("server health: {}", server);
        healthCheckExecutor.submit(() -> {
            try {
                String healthUrl = server.getBaseUrl() + "/actuator/health";
                restTemplate.getForObject(healthUrl, String.class);

                // 更新服务器状态
                server.setStatus(McpServer.ServerStatus.HEALTHY);
                server.setLastHealthCheckTime(Instant.now());
                
                logger.debug("Health check successful for server: {}", server);
            } catch (Exception e) {
                // 记录错误并更新服务器状态
                server.setStatus(McpServer.ServerStatus.UNAVAILABLE);
                logger.warn("Health check failed for server: {} - Error: {}", server, e.getMessage());
            }
        });
    }

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void periodicHealthCheck() {
        servers.values().forEach(this::performHealthCheck);
        
        // 清理长期不可用的服务器
        List<String> serversToRemove = servers.values().stream()
                .filter(McpServer::shouldBeRemoved)
                .map(McpServer::getId)
                .collect(Collectors.toList());
        
        serversToRemove.forEach(this::unregister);
        
        if (!serversToRemove.isEmpty()) {
            logger.info("Removed {} unavailable servers", serversToRemove.size());
        }
    }

    // 提供服务器状态变更的通知机制
    @Override
    public List<McpServer> getServersWithStatusChange(Instant since) {
        return servers.values().stream()
            .filter(server -> server.getLastStatusChangeTime().isAfter(since))
            .collect(Collectors.toList());
    }
} 