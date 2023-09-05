package com.sivalabs.bookstore.common;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductRepository;
import com.sivalabs.bookstore.notifications.NotificationService;
import com.sivalabs.bookstore.payment.domain.CreditCard;
import com.sivalabs.bookstore.payment.domain.CreditCardRepository;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @MockBean
    protected NotificationService notificationService;

    protected final List<Product> products = List.of(
            new Product(null, "P100", "Product 1", "Product 1 desc", null, BigDecimal.TEN),
            new Product(null, "P101", "Product 2", "Product 2 desc", null, BigDecimal.valueOf(24)));

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();
        productRepository.saveAll(products);
        creditCardRepository.deleteAllInBatch();

        creditCardRepository.save(new CreditCard(null, "Siva", "1111222233334444", "123", 2, 2030));
        creditCardRepository.save(new CreditCard(null, "John", "1234123412341234", "123", 3, 2030));
    }
}
