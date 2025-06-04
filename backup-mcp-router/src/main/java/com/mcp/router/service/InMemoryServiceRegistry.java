package com.mcp.router.service;

import com.mcp.router.model.McpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class InMemoryServiceRegistry {
    private final Map<String, McpServer> registeredServers = new ConcurrentHashMap<>();

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
                                    List<String> supportedTools, Map<String, Object> metadata) {
        String serviceUrl = String.format("http://%s:%d", host, port);
        
        McpServer server = McpServer.builder()
            .name(serverName)
            .host(host)
            .port(port)
            .serviceUrl(serviceUrl)
            .supportedTools(supportedTools)
            .metadata(metadata)
            .status(McpServer.ServerStatus.HEALTHY)
            .loadFactor(0.0)
            .build();
        
        registeredServers.put(serverName, server);
        log.info("Registered server: {}", server);
        return server;
    }

    /**
     * Unregister a server by its name
     * 
     * @param serverName Name of the server to unregister
     * @return True if server was found and unregistered, false otherwise
     */
    public boolean unregisterServer(String serverName) {
        McpServer removedServer = registeredServers.remove(serverName);
        if (removedServer != null) {
            log.info("Unregistered server: {}", removedServer);
            return true;
        }
        return false;
    }

    /**
     * Discover all registered MCP servers
     * 
     * @return List of registered servers
     */
    public List<McpServer> discoverMcpServers() {
        return new ArrayList<>(registeredServers.values());
    }

    /**
     * Find a server by its name
     * 
     * @param serverName Name of the server to find
     * @return Optional containing the server if found
     */
    public Optional<McpServer> findServerByName(String serverName) {
        return Optional.ofNullable(registeredServers.get(serverName));
    }

    /**
     * Initialize default servers (for backward compatibility)
     */
    public void initDefaultServers() {
        // Add initial servers if needed
        McpServer weatherServer = McpServer.builder()
            .name("mcp-server-weather")
            .host("localhost")
            .port(18081)
            .serviceUrl("http://localhost:18081")
            .supportedTools(List.of("getCityWeather"))
            .status(McpServer.ServerStatus.HEALTHY)
            .loadFactor(0.3)
            .build();
        
        McpServer bookServer = McpServer.builder()
            .name("mcp-server-book")
            .host("localhost")
            .port(18082)
            .serviceUrl("http://localhost:18082")
            .supportedTools(List.of("getBookDetails", "findBooksByAuthor"))
            .status(McpServer.ServerStatus.HEALTHY)
            .loadFactor(0.5)
            .build();
        
        registeredServers.put(weatherServer.getName(), weatherServer);
        registeredServers.put(bookServer.getName(), bookServer);
    }
} 