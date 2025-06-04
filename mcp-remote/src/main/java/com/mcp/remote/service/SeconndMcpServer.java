package com.mcp.remote.service;

import com.mcp.sdk.annotation.McpServerTool;
import com.mcp.sdk.service.McpService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SeconndMcpServer implements McpService {

    @Override
    public String getServiceName() {
        return "SecondMcpServer";
    }

    @Override
    public List<String> getSupportedTools() {
        return Arrays.asList("secondServerTool");
    }

    @Override
    public Object executeTask(String toolName, Object input) {
        return "Executed task: " + toolName + " with input: " + input;
    }

    @McpServerTool(
        name = "secondServerTool", 
        description = "Second MCP Server Tool", 
        priority = 1, 
        enabled = true
    )
    public Object defaultServiceMethod() {
        return "Second MCP Server Tool Response";
    }


    @McpServerTool(
            name = "secondServerTool2",
            description = "Second MCP Server Tool2",
            priority = 1,
            enabled = true
    )
    public Object defaultServiceMethod2() {
        return "Second MCP Server Tool Response2";
    }
}