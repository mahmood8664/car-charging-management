package fi.develon.ev.repository;

import fi.develon.ev.entity.Station;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Station repository
 *
 * @author mahmood
 * @since 9/10/21
 */
@Repository
public interface StationRepository extends MongoRepository<Station, Long> {

    Slice<Station> findByLocationNear(GeoJsonPoint location, Distance d, Pageable pageable);

    List<Station> findAllByCompanyIdIn(List<String> companyIds);

}
