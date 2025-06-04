package com.mcp.router.controller;

import com.alibaba.fastjson.JSON;
import com.mcp.router.service.McpServiceRegistry;
import com.mcp.sdk.model.McpServer;
import com.mcp.sdk.processor.McpServerToolProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/router")
public class McpRouterController {

    private static final Logger logger = LoggerFactory.getLogger(McpRouterController.class);

    @Autowired
    private McpServiceRegistry serviceRegistry;

    @PostMapping("/register")
    public ResponseEntity<String> registerServer(@RequestBody String requestBody) {

        logger.info("requestBody: {}", requestBody);
        McpServer mcpServer = JSON.parseObject(requestBody, McpServer.class);
        serviceRegistry.register(mcpServer);
        return ResponseEntity.ok("Server registered successfully: " + mcpServer.getId());
    }

    @DeleteMapping("/unregister/{serverId}")
    public ResponseEntity<String> unregisterServer(@PathVariable String serverId) {
        serviceRegistry.unregister(serverId);
        return ResponseEntity.ok("Server unregistered: " + serverId);
    }

    @GetMapping("/servers")
    public ResponseEntity<List<McpServer>> getAllServers() {
        return ResponseEntity.ok(serviceRegistry.getAllServers());
    }

    @GetMapping("/select/{tool}")
    public ResponseEntity<McpServer> selectBestServer(@PathVariable String tool) {
        McpServer bestServer = serviceRegistry.selectBestServer(tool);
        if (bestServer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bestServer);
    }

    @GetMapping("/status-changes")
    public ResponseEntity<List<McpServer>> getStatusChanges(
            @RequestParam(value = "since", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
            Instant since) {
        
        if (since == null) {
            since = Instant.now().minusSeconds(300);
        }
        
        List<McpServer> changedServers = serviceRegistry.getServersWithStatusChange(since);
        return ResponseEntity.ok(changedServers);
    }
} 