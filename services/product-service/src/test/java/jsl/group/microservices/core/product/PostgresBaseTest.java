package jsl.group.microservices.core.product;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresBaseTest {
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres").withStartupTimeoutSeconds(300);

    static { postgreSQLContainer.start(); }

    @DynamicPropertySource
    public static void datasource(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }
}
