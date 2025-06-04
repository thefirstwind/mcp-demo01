package com.mcp.servertwo.service;

import com.mcp.sdk.annotation.McpServerTool;
import com.mcp.sdk.service.McpService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ServerTwoService implements McpService {

    @Override
    public String getServiceName() {
        return "McpServerTwo";
    }

    @Override
    public List<String> getSupportedTools() {
        return Arrays.asList("serverTwoTool1", "serverTwoTool2");
    }

    @Override
    public Object executeTask(String toolName, Object input) {
        switch (toolName) {
            case "serverTwoTool1":
                return executeServerTwoToolOne(input);
            case "serverTwoTool2":
                return executeServerTwoToolTwo(input);
            default:
                throw new UnsupportedOperationException("Tool " + toolName + " is not supported");
        }
    }

    @McpServerTool(
        name = "serverTwoTool1", 
        description = "First tool of Server Two", 
        priority = 1, 
        enabled = true
    )
    public Object executeServerTwoToolOne(Object input) {
        return "Server Two Tool One executed with input: " + input;
    }

    @McpServerTool(
        name = "serverTwoTool2", 
        description = "Second tool of Server Two", 
        priority = 2, 
        enabled = true
    )
    public Object executeServerTwoToolTwo(Object input) {
        return "Server Two Tool Two executed with input: " + input;
    }

    @McpServerTool(
        name = "defaultServerTwoTool", 
        description = "Default tool for Server Two", 
        priority = 0, 
        enabled = true
    )
    public Object defaultServiceMethod() {
        return "Default method for Server Two";
    }
} 