package com.sivalabs.bookstore.common;

import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductRepository;
import com.sivalabs.bookstore.notifications.NotificationService;
import com.sivalabs.bookstore.payment.domain.CreditCard;
import com.sivalabs.bookstore.payment.domain.CreditCardRepository;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3-alpine");
    protected static final MongoDBContainer mongodb = new MongoDBContainer("mongo:6.0");
    protected static final KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
    protected static final GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:7.0.11-alpine")).withExposedPorts(6379);

    @LocalServerPort
    private Integer port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @MockBean
    protected NotificationService notificationService;

    protected final List<Product> products = List.of(
            new Product(null, "P100", "Product 1", "Product 1 desc", null, BigDecimal.TEN, BigDecimal.valueOf(2.5)),
            new Product(null, "P101", "Product 2", "Product 2 desc", null, BigDecimal.valueOf(24), BigDecimal.ZERO));

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();
        productRepository.saveAll(products);
        creditCardRepository.deleteAllInBatch();

        creditCardRepository.save(new CreditCard(null, "Siva", "1111222233334444", "123", 2, 2030));
        creditCardRepository.save(new CreditCard(null, "John", "1234123412341234", "123", 3, 2030));
    }

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(mongodb, postgres, redis, kafka).join();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongodb::getReplicaSetUrl);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}
