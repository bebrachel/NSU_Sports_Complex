package ru.nsu.sports.complex.backend.flyway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class FlywayVersionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testAllMigrationsApplied() {
        Integer appliedMigrations = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = true", Integer.class);
        Assertions.assertNotNull(appliedMigrations, "Applied migrations count should not be null");
        Assertions.assertTrue(appliedMigrations > 0, "At least one migration should have been applied");
    }
}
