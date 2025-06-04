package com.mcp.sdk;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public interface McpServerRegistrar {
    /**
     * Register the current server with the MCP router
     * 
     * @param serverName Name of the server
     * @param host Host address
     * @param port Server port
     * @param supportedTools List of tools supported by this server
     * @param metadata Additional server metadata
     */
    void registerServer(String serverName, String host, int port, 
                        String[] supportedTools, Map<String, Object> metadata);

    /**
     * Unregister the current server from the MCP router
     */
    void unregisterServer();

    /**
     * Get the local host address
     * 
     * @return Local host address as a string
     * @throws UnknownHostException If host address cannot be determined
     */
    default String getLocalHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Create a default metadata map with system information
     * 
     * @return Map of system metadata
     */
    default Map<String, Object> createDefaultMetadata() {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("java.version", System.getProperty("java.version"));
        metadata.put("os.name", System.getProperty("os.name"));
        metadata.put("os.arch", System.getProperty("os.arch"));
        metadata.put("user.timezone", System.getProperty("user.timezone"));
        return metadata;
    }

    /**
     * Validate server registration parameters
     * 
     * @param serverName Name of the server
     * @param host Host address
     * @param port Server port
     * @param supportedTools List of tools supported by this server
     * @throws IllegalArgumentException If parameters are invalid
     */
    default void validateRegistrationParameters(String serverName, String host, int port, 
                                                String[] supportedTools) {
        if (serverName == null || serverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be null or empty");
        }
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("Host address cannot be null or empty");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port number: " + port);
        }
        if (supportedTools == null || supportedTools.length == 0) {
            throw new IllegalArgumentException("At least one supported tool must be specified");
        }
    }
} 