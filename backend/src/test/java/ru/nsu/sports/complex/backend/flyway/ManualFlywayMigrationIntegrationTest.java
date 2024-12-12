package ru.nsu.sports.complex.backend.flyway;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManualFlywayMigrationIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Test
    void skipAutomaticAndTriggerManualFlywayMigration() {
        Assertions.assertTrue(flyway.migrate().success);
    }
}