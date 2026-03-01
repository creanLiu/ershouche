package com.usedcar.module.system.controller;

import com.usedcar.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Tag(name = "健康检查", description = "系统健康检查接口")
@RestController
public class HealthCheckController {

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "健康检查")
    @GetMapping("/api/health")
    public R<Map<String, Object>> health() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("service", "usedcar-server");
        info.put("version", "1.0.0-SNAPSHOT");
        info.put("time", LocalDateTime.now().toString());

        // MySQL 连接检查
        String mysqlStatus = "NOT_CONFIGURED";
        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                mysqlStatus = conn.isValid(3) ? "UP" : "DOWN";
            } catch (Exception e) {
                mysqlStatus = "DOWN: " + e.getMessage();
                log.warn("MySQL健康检查失败: {}", e.getMessage());
            }
        }
        info.put("mysql", mysqlStatus);

        // Redis 连接检查
        String redisStatus = "NOT_CONFIGURED";
        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().get("health:check");
                redisStatus = "UP";
            } catch (Exception e) {
                redisStatus = "DOWN: " + e.getMessage();
                log.warn("Redis健康检查失败: {}", e.getMessage());
            }
        }
        info.put("redis", redisStatus);

        boolean allUp = "UP".equals(mysqlStatus) && "UP".equals(redisStatus);
        info.put("status", allUp ? "UP" : "DEGRADED");

        return R.ok(info);
    }
}
