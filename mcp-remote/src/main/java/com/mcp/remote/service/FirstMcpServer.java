package com.mcp.remote.service;

import com.mcp.sdk.annotation.McpServerTool;
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
        return Arrays.asList("firstServerTool");
    }

    @Override
    public Object executeTask(String toolName, Object input) {
        return "Executed task: " + toolName + " with input: " + input;
    }

    @McpServerTool(
        name = "firstServerTool",
        description = "First MCP Server Tool",
        priority = 1,
        enabled = true
    )
    public Object defaultServiceMethod() {
        return "First MCP Server Tool Response";
    }

    @McpServerTool(
            name = "firstServerTool2",
            description = "First MCP Server Tool",
            priority = 1,
            enabled = true
    )
    public Object defaultServiceMethod2() {
        return "First MCP Server Tool Response";
    }
    @McpServerTool(
            name = "firstServerTool3",
            description = "First MCP Server Tool",
            priority = 1,
            enabled = true
    )
    public Object defaultServiceMethod3() {
        return "First MCP Server Tool Response";
    }

}