package com.storage.filestorageservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FileStorageServiceApplicationIT extends AbstractIntegrationTests {

    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    SessionRepository sessionRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void usersTableExists() {
        assertThat(jdbc.queryForObject("select count(*) from users", Long.class)).isNotNegative();
    }

    @Test
    void sessionsAreStoredInRedis() {
        assertThat(sessionRepository).isInstanceOf(RedisSessionRepository.class);

        Session session = sessionRepository.createSession();
        sessionRepository.save(session);
        assertThat(sessionRepository.findById(session.getId())).isNotNull();
    }

}
