package pl.project.wwsis.ecommerceshop;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class BaseIT {

    @Container
    private static MySQLContainer<?> mySQLContainer =
            new MySQLContainer<>("8.0-oracle")
                    .withDatabaseName("mysql")
                    .withUsername("mysql")
                    .withPassword("mysql");
    @DynamicPropertySource
    public static void containerConfig(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    }
}
