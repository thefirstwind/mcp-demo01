package com.mcp.sdk.service;

import com.mcp.sdk.annotation.McpServerTool;

import java.util.List;

public interface McpService {
    String getServiceName();

    List<String> getSupportedTools();

    Object executeTask(String toolName, Object input);

    /**
     * 默认的 MCP 服务方法
     * 子类可以通过重写此方法并添加 @McpServerTool 注解来定义服务工具
     * 
     * @return 服务执行结果
     */
    default Object defaultServiceMethod() {
        return "Default MCP Service Method";
    }
} 