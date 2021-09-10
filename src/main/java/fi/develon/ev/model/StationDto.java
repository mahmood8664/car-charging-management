package fi.develon.ev.model;

import lombok.Builder;
import lombok.Value;

/**
 * represent a station
 * @author mahmood
 * @since 9/10/21
 */
@Value
@Builder
public class StationDto {
    Long id;
    String name;
    int latitude;
    int longitude;
    Long companyId;
}
