package com.mcp.remote.service;

import com.mcp.sdk.service.McpService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FirstMcpServer implements McpService {
    @Override
    public String getServiceName() {
        return "FirstMcpServer";
    }

    @Override
    public List<String> getSupportedTools() {
        return Arrays.asList("tool1", "tool2", "tool3");
    }

    @Override
    public Object executeTask(String toolName, Object input) {
        switch (toolName) {
            case "tool1":
                return executeTool1(input);
            case "tool2":
                return executeTool2(input);
            case "tool3":
                return executeTool3(input);
            default:
                throw new UnsupportedOperationException("Tool " + toolName + " is not supported");
        }
    }

    private Object executeTool1(Object input) {
        // Example implementation
        return "Executed tool1 with input: " + input;
    }

    private Object executeTool2(Object input) {
        // Example implementation
        return "Executed tool2 with input: " + input;
    }

    private Object executeTool3(Object input) {
        // Example implementation
        return "Executed tool3 with input: " + input;
    }
} 