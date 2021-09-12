package fi.develon.ev.tdd;

import fi.develon.ev.repository.CompanyRepository;
import fi.develon.ev.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mahmood
 * @since 9/11/21
 */
@SpringBootTest
public class StationControllerTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    @Transactional
    void clearData(){
        stationRepository.deleteAll();
        companyRepository.deleteAll();
    }



}
