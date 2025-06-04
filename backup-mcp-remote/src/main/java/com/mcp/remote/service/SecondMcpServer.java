package com.mcp.remote.service;

import com.mcp.sdk.annotation.McpServerTool;
import com.mcp.sdk.service.McpService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SecondMcpServer implements McpService {
    @Override
    public String getServiceName() {
        return "SecondMcpServer";
    }

    @Override
    public List<String> getSupportedTools() {
        return Arrays.asList("advanced-tool1", "advanced-tool2", "advanced-tool3");
    }

    @Override
    public Object executeTask(String toolName, Object input) {
        switch (toolName) {
            case "advanced-tool1":
                return advancedTool1(input);
            case "advanced-tool2":
                return advancedTool2(input);
            case "advanced-tool3":
                return advancedTool3(input);
            default:
                throw new UnsupportedOperationException("Tool " + toolName + " is not supported");
        }
    }

    @McpServerTool(
        name = "advanced-tool1", 
        description = "An advanced tool for complex operations",
        priority = 10
    )
    public String advancedTool1(Object input) {
        // Example implementation with custom annotation
        return "Executed advanced-tool1 with input: " + input + 
               " - This tool has high priority and advanced capabilities";
    }

    @McpServerTool(
        name = "advanced-tool2", 
        description = "A specialized tool for specific tasks",
        enabled = false
    )
    public String advancedTool2(Object input) {
        // Example of a disabled tool
        return "Attempted to execute advanced-tool2, but it is currently disabled";
    }

    @McpServerTool(
        name = "advanced-tool3", 
        description = "A flexible tool with multiple use cases",
        priority = 5
    )
    public String advancedTool3(Object input) {
        // Another example implementation
        return "Executed advanced-tool3 with input: " + input + 
               " - This tool offers versatile functionality";
    }
} 