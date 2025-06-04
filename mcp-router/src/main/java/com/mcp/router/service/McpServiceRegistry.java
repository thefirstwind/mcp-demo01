package com.mcp.router.service;

import com.mcp.sdk.model.McpServer;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

public interface McpServiceRegistry {
    void register(McpServer server);
    void unregister(String serverId);
    List<McpServer> getAllServers();
    Optional<McpServer> findServerById(String serverId);
    List<McpServer> findServersByTool(String tool);
    McpServer selectBestServer(String tool);
    List<McpServer> getServersWithStatusChange(Instant since);
} 