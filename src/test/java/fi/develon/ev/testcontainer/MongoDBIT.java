package fi.develon.ev.testcontainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.develon.ev.repository.CompanyRepository;
import fi.develon.ev.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author mahmood
 * @since 9/12/21
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext
@Slf4j
@Testcontainers
public class MongoDBIT {

    @Autowired
    protected CompanyRepository companyRepository;
    @Autowired
    protected StationRepository stationRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper json;

    @BeforeEach
    void beforeEach() {
        clearData();
    }

    private void clearData() {
        stationRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

}
