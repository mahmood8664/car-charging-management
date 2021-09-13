package fi.develon.ev.repository;

import fi.develon.ev.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Company repository
 *
 * @author mahmood
 * @since 9/10/21
 */
@Repository
public interface CompanyRepository extends MongoRepository<Company, Long> {

    @Query("{}")
    Slice<Company> findAllCompanies(Pageable pageable);

}
