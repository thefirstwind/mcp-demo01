package com.mcp.router.controller;

import com.mcp.router.model.McpServer;
import com.mcp.router.service.McpServerDiscoveryService;
import com.mcp.router.service.McpServerRouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mcp-router")
@Slf4j
public class McpRouterController {
    private final McpServerDiscoveryService discoveryService;
    private final McpServerRouterService routerService;

    public McpRouterController(
        McpServerDiscoveryService discoveryService,
        McpServerRouterService routerService
    ) {
        this.discoveryService = discoveryService;
        this.routerService = routerService;
    }

    @GetMapping("/servers")
    public ResponseEntity<List<McpServer>> getAllServers() {
        List<McpServer> servers = discoveryService.discoverMcpServers();
        return ResponseEntity.ok(servers);
    }

    @GetMapping("/servers/tool/{toolName}")
    public ResponseEntity<List<McpServer>> getServersByTool(@PathVariable String toolName) {
        List<McpServer> servers = routerService.listSupportedServers(toolName);
        return ResponseEntity.ok(servers);
    }

    @GetMapping("/server/best-for-tool/{toolName}")
    public ResponseEntity<McpServer> getBestServerForTool(@PathVariable String toolName) {
        Optional<McpServer> bestServer = routerService.selectBestServer(toolName);
        return bestServer
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/servers/balance")
    public ResponseEntity<List<McpServer>> balanceServerSelection(
        @RequestBody List<String> requiredTools
    ) {
        try {
            List<McpServer> selectedServers = routerService.balanceServerSelection(requiredTools);
            return ResponseEntity.ok(selectedServers);
        } catch (IllegalStateException e) {
            log.error("Server selection error", e);
            return ResponseEntity.badRequest().build();
        }
    }
} 