package fi.develon.ev.model;

import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.Station;

import java.math.BigDecimal;

/**
 * @author mahmood
 * @since 9/13/21
 */
public class DtoMapper {

    private DtoMapper() {
    }

    public static CompanyDto getCompanyDto(Company company) {
        return CompanyDto.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .parentCompanyId(company.getParentCompanyId())
                .build();
    }

    public static StationDto getStationDto(Station station) {
        return StationDto.builder()
                .stationId(station.getId())
                .stationName(station.getName())
                .companyId(station.getCompanyId())
                .latitude(BigDecimal.valueOf(station.getLocation().getY()))
                .longitude(BigDecimal.valueOf(station.getLocation().getX()))
                .build();
    }
}
