package fi.develon.ev.repository;

import fi.develon.ev.entity.Station;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Station repository
 *
 * @author mahmood
 * @since 9/10/21
 */
@Repository
public interface StationRepository extends MongoRepository<Station, Long> {

    GeoResults<Station> findByLocationNear(Point p, Distance d);

}
