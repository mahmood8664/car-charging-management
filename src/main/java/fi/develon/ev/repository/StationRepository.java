package fi.develon.ev.repository;

import fi.develon.ev.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Station repository
 * @author mahmood
 * @since 9/10/21
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
}
