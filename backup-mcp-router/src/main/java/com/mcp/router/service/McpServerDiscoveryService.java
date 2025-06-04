package com.mcp.router.service;

import com.mcp.router.model.McpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class McpServerDiscoveryService {
    private final InMemoryServiceRegistry serviceRegistry;

    /**
     * Discover all registered MCP servers
     * 
     * @return List of registered servers
     */
    public List<McpServer> discoverMcpServers() {
        return serviceRegistry.discoverMcpServers();
    }

    /**
     * Register a new MCP server
     * 
     * @param serverName Name of the server
     * @param host Host address
     * @param port Server port
     * @param supportedTools List of tools supported by the server
     * @param metadata Additional server metadata
     * @return Registered McpServer instance
     */
    public McpServer registerServer(String serverName, String host, int port, 
                                    List<String> supportedTools, 
                                    java.util.Map<String, Object> metadata) {
        return serviceRegistry.registerServer(serverName, host, port, supportedTools, metadata);
    }

    /**
     * Unregister a server by its name
     * 
     * @param serverName Name of the server to unregister
     * @return True if server was found and unregistered, false otherwise
     */
    public boolean unregisterServer(String serverName) {
        return serviceRegistry.unregisterServer(serverName);
    }
} 