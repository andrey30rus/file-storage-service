package com.storage.filestorageservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import com.redis.testcontainers.RedisContainer;

public class AbstractIntegrationTests {
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");
    static final RedisContainer REDIS = new RedisContainer("redis:7-alpine");
    static {
        POSTGRES.start();
        REDIS.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        r.add("spring.datasource.username", POSTGRES::getUsername);
        r.add("spring.datasource.password", POSTGRES::getPassword);
        r.add("spring.data.redis.host", REDIS::getRedisHost);
        r.add("spring.data.redis.port", REDIS::getRedisPort);
    }

}
