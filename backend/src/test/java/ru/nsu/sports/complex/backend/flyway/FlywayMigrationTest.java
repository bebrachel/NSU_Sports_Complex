package ru.nsu.sports.complex.backend.flyway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Для предварительной инициализации данных
class FlywayMigrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testSchemaIntegritySections() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            String tableSections = "sections";
            // Проверка, что таблица "sections" существует
            try (ResultSet tables = metaData.getTables(null, null, tableSections, null)) {
                Assertions.assertTrue(tables.next(), "Table '" + tableSections + "' should exist");
            }

            // Проверка, что колонка "name" есть в таблице "sections"
            try (ResultSet columns = metaData.getColumns(null, null, tableSections, "name")) {
                Assertions.assertTrue(columns.next(), "Column 'name' in table '" + tableSections + "' should exist");
            }

            // Проверка, что foreignKey "schedule_id" есть в таблице "sections"
            try (ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableSections)) {
                boolean scheduleForeignKeyExists = false;
                while (foreignKeys.next()) {
                    if ("schedule_id".equals(foreignKeys.getString("FKCOLUMN_NAME"))) {
                        scheduleForeignKeyExists = true;
                    }
                }
                Assertions.assertTrue(scheduleForeignKeyExists, "Foreign key 'schedule_id' should exist in '" + tableSections + "'");
            }
        }
    }

    @Test
    void testSchemaIntegrityUsers() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            String tableUsers = "users";
            // Проверка, что таблица "users" существует
            try (ResultSet tables = metaData.getTables(null, null, tableUsers, null)) {
                Assertions.assertTrue(tables.next(), "Table '" + tableUsers + "' should exist");
            }

            // Проверка, что колонка "name" есть в таблице "users"
            try (ResultSet columns = metaData.getColumns(null, null, tableUsers, "name")) {
                Assertions.assertTrue(columns.next(), "Column 'name' in table '" + tableUsers + "' should exist");
            }
        }
    }
}
