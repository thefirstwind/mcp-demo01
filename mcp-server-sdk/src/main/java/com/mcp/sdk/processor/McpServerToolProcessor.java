package com.mcp.sdk.processor;

import com.alibaba.fastjson2.JSON;
import com.mcp.sdk.annotation.McpServerTool;
import com.mcp.sdk.model.McpServer;
import com.mcp.sdk.service.McpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class McpServerToolProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(McpServerToolProcessor.class);

    private static final Map<String, List<McpServerToolInfo>> discoveredToolMap = new HashMap<String, List<McpServerToolInfo>>();

    @Value("${server.port}") // 读取 my.property.name 字段
    private String remoteServerPort;

    @Value("${mcp.router.url}") // 读取 my.property.name 字段
    private String mcpRouterUrl;

    //    @Resource
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 扫描所有方法，找到带有 @McpServerTool 注解的方法
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            McpServerTool annotation = AnnotationUtils.findAnnotation(method, McpServerTool.class);
            if (annotation != null) {
//                logger.info("McpServerTool: {}", annotation);
                McpServerToolInfo toolInfo = new McpServerToolInfo(
                        beanName,
                        bean.getClass(),
                        method,
                        annotation
                );
                if (!discoveredToolMap.containsKey(beanName)) {
                    discoveredToolMap.put(beanName, new ArrayList<>());
                }
                List<McpServerToolInfo> discoveredTools = discoveredToolMap.get(beanName);

                if (!toolInfo.getAnnotation().enabled()) {
                    continue;
                }
                if (discoveredTools.stream().filter(p -> p.getMethod().equals(method)).findAny().isPresent()) {
                    continue;
                }
                discoveredToolMap.get(beanName).add(toolInfo);
            }

        }


        if(discoveredToolMap.containsKey(beanName)) {
//            logger.info("discoveredToolMap:{},{}", beanName, JSON.toJSONString(discoveredToolMap));

            for (String key : discoveredToolMap.keySet()) {
                List<McpServerToolInfo> toolInfos = discoveredToolMap.get(key);
                logger.info("toolInfos:{},{}", key, JSON.toJSONString(toolInfos));

                try {
                    String host = InetAddress.getLocalHost().getHostAddress();
                    int port = Integer.valueOf(remoteServerPort); // 确保与application.yml中的端口一致

                    // 准备服务器元数据
                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("java.version", System.getProperty("java.version"));
                    metadata.put("os.name", System.getProperty("os.name"));
                    metadata.put("os.arch", System.getProperty("os.arch"));

                    // 支持的工具列表
                    String[] supportedTools = toolInfos.stream().map(p -> {
                        McpServerTool serverTool = p.getAnnotation();
                        return serverTool.name();
                    }).collect(Collectors.toList()).toArray(new String[0]);

                    // 发送注册请求到路由器
                    String registrationUrl = mcpRouterUrl + "/register"; // 假设路由器监听8080端口
                    McpServer registrationPayload = new McpServer(
                            key,
                            host,
                            port,
                            supportedTools,
                            metadata,
                            McpServer.ServerStatus.STARTING
                    );
                    restTemplate.postForObject(registrationUrl, JSON.toJSONString(registrationPayload), String.class);
                    logger.info("Server registered successfully with MCP router");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Failed to register server with MCP router: {}" ,e.getMessage());
                }

            }
        }
        return bean;
    }


//    /**
//     * 获取所有已发现的工具信息
//     */
//    public List<McpServerToolInfo> getDiscoveredTools() {
//        return new ArrayList<>(discoveredTools);
//    }

    /**
     * 内部类，用于存储工具信息
     */
    public static class McpServerToolInfo {
        private final String beanName;
        private final Class<?> beanClass;
        private final Method method;
        private final McpServerTool annotation;

        public McpServerToolInfo(String beanName, Class<?> beanClass, Method method, McpServerTool annotation) {
            this.beanName = beanName;
            this.beanClass = beanClass;
            this.method = method;
            this.annotation = annotation;
        }

        // Getters
        public String getBeanName() { return beanName; }
        public Class<?> getBeanClass() { return beanClass; }
        public Method getMethod() { return method; }
        public McpServerTool getAnnotation() { return annotation; }
    }
} 