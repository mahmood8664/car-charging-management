package fi.develon.ev.tdd;

import fi.develon.ev.entity.Company;
import fi.develon.ev.repository.CompanyRepository;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author mahmood
 * @since 9/11/21
 */
public class StationControllerTest extends MongoDBIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testtt() {
        List<Company> all = companyRepository.findAll();
        System.out.println(all);

    }

}
