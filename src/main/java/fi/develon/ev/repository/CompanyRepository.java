package fi.develon.ev.repository;

import fi.develon.ev.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Company repository
 *
 * @author mahmood
 * @since 9/10/21
 */
@Repository
public interface CompanyRepository extends MongoRepository<Company, Long> {
}
