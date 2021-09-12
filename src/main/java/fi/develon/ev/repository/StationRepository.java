package fi.develon.ev.repository;

import fi.develon.ev.entity.Station;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Station repository
 *
 * @author mahmood
 * @since 9/10/21
 */
@Repository
public interface StationRepository extends MongoRepository<Station, Long> {
}
