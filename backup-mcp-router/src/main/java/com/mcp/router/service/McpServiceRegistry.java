package com.mcp.router.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class McpServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(McpServiceRegistry.class);

    // 存储所有注册的服务
    private final Map<String, Object> registeredServices = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 在应用上下文刷新时自动发现并注册服务
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 查找所有带有 @Service 注解的 Bean
        Map<String, Object> services = applicationContext.getBeansWithAnnotation(org.springframework.stereotype.Service.class);
        
        // 记录新发现的服务
        List<Object> newServices = new ArrayList<>();
        
        services.forEach((beanName, service) -> {
            // 尝试获取服务名称
            String serviceName = getServiceName(service);
            
            if (serviceName != null && !registeredServices.containsKey(serviceName)) {
                // 新服务
                registeredServices.put(serviceName, service);
                newServices.add(service);
            }
        });

        // 打印新发现的服务
        if (!newServices.isEmpty()) {
            logger.info("=== New Services Discovered ===");
            newServices.forEach(service -> {
                logger.info("Service Name: {}", getServiceName(service));
                logger.info("Service Class: {}", service.getClass().getSimpleName());
                logger.info("---");
            });
            logger.info("Total Registered Services: {}", registeredServices.size());
        }
    }

    /**
     * 尝试通过反射获取服务名称
     */
    private String getServiceName(Object service) {
        try {
            Method getServiceNameMethod = service.getClass().getMethod("getServiceName");
            return (String) getServiceNameMethod.invoke(service);
        } catch (Exception e) {
            // 如果没有 getServiceName 方法，返回 null
            return null;
        }
    }

    /**
     * 获取所有已注册的服务
     */
    public Map<String, Object> getRegisteredServices() {
        return new ConcurrentHashMap<>(registeredServices);
    }

    /**
     * 根据服务名获取服务
     */
    public Object getServiceByName(String serviceName) {
        return registeredServices.get(serviceName);
    }
} 