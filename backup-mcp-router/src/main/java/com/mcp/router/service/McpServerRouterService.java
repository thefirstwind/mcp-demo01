package com.mcp.router.service;

import com.mcp.router.model.McpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class McpServerRouterService {
    private final McpServerDiscoveryService discoveryService;

    public McpServerRouterService(McpServerDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    /**
     * 根据工具名称选择最适合的 MCP 服务器
     * 
     * @param requiredTool 需要的工具名称
     * @return 最适合的 MCP 服务器
     */
    public Optional<McpServer> selectBestServer(String requiredTool) {
        List<McpServer> availableServers = discoveryService.discoverMcpServers();
        
        return availableServers.stream()
            .filter(server -> server.getSupportedTools().contains(requiredTool))
            .max(Comparator
                .comparingDouble(server -> ((McpServer)server).calculateSuitability(requiredTool))
                .thenComparing(server -> ((McpServer)server).getLoadFactor())
            );
    }

    /**
     * 获取所有支持特定工具的服务器列表
     * 
     * @param requiredTool 需要的工具名称
     * @return 支持该工具的服务器列表
     */
    public List<McpServer> listSupportedServers(String requiredTool) {
        List<McpServer> availableServers = discoveryService.discoverMcpServers();
        
        return availableServers.stream()
            .filter(server -> server.getSupportedTools().contains(requiredTool))
            .sorted(Comparator
                .comparingDouble(server -> ((McpServer)server).calculateSuitability(requiredTool))
                .reversed()
            )
            .toList();
    }

    /**
     * 根据负载和状态平衡选择服务器
     * 
     * @param requiredTools 需要的工具列表
     * @return 最佳服务器组合
     */
    public List<McpServer> balanceServerSelection(List<String> requiredTools) {
        List<McpServer> availableServers = discoveryService.discoverMcpServers();
        
        return requiredTools.stream()
            .map(tool -> selectBestServer(tool)
                .orElseThrow(() -> new IllegalStateException("No server found for tool: " + tool))
            )
            .toList();
    }
} 