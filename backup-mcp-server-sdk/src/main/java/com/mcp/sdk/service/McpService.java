package com.mcp.sdk.service;

import java.util.List;

public interface McpService {
    /**
     * Get the name of the service
     * 
     * @return Service name
     */
    String getServiceName();

    /**
     * Get the list of tools supported by this service
     * 
     * @return List of supported tool names
     */
    List<String> getSupportedTools();

    /**
     * Perform a specific task using the given tool
     * 
     * @param toolName Name of the tool to use
     * @param input Input parameters for the tool
     * @return Result of the tool execution
     */
    Object executeTask(String toolName, Object input);
} 