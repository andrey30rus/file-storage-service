package com.storage.filestorageservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FileStorageServiceApplicationIT extends AbstractIntegrationTests {

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void contextLoads() {
    }

    @Test
    void usersTableExists() {
        assertThat(jdbc.queryForObject("select count(*) from users", Long.class)).isNotNegative();
    }

}
