package fi.develon.ev.service;

import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.Station;
import fi.develon.ev.exception.SMException;
import fi.develon.ev.exception.SMExceptionType;
import fi.develon.ev.model.*;
import fi.develon.ev.repository.CompanyRepository;
import fi.develon.ev.repository.StationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Stations' services
 *
 * @author mahmood
 * @since 9/10/21
 */
@Service
@AllArgsConstructor
public class StationService {

    private final StationRepository stationRepository;
    private final CompanyRepository companyRepository;

    /**
     * find station by given creteria, this service is paginated.
     *
     * @param request {@link PaginationRequest}
     * @return {@link PagingResponse<StationDto>}
     */
    public PagingResponse<StationDto> findStations(PaginationRequest request) {

        Page<Station> stations = stationRepository.findAll(Example.of(Station.builder()
                        .companyId(request.getCompanyId())
                        .build()),
                PageRequest.of(request.getPageNumber(), request.getSize()));

        return PagingResponse.of(stations.get().map(DtoMapper::getStationDto).collect(Collectors.toList()), stations.hasNext());

    }

    /**
     * find a station
     *
     * @param stationId station id
     * @return {@link StationDto}
     */
    public StationDto getStation(String stationId) {

        return DtoMapper.getStationDto(stationRepository.findOne(Example.of(Station.builder()
                .id(stationId)
                .build())).orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND)));

    }


    /**
     * Create a station
     *
     * @param request {@link CreateStationRequest}
     * @return created station id
     */
    public String createStation(CreateStationRequest request) {
        companyRepository.findOne(Example.of(Company.builder().id(request.getCompanyId()).build()))
                .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));

        return stationRepository.save(Station.builder()
                .name(request.getStationName())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .companyId(request.getCompanyId())
                .build()).getId();

    }

    /**
     * Update a station
     *
     * @param request {@link StationDto}
     */
    public void updateStation(StationDto request) {

        Station station = stationRepository.findOne(Example.of(Station.builder().id(request.getStationId()).build()))
                .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));

        station.setLatitude(request.getLatitude());
        station.setLongitude(request.getLongitude());
        station.setName(request.getStationName());

        if (!station.getCompanyId().equals(request.getCompanyId())) {
            companyRepository.findOne(Example.of(Company.builder().id(request.getCompanyId()).build()))
                    .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));
            station.setCompanyId(request.getCompanyId());
        }

        stationRepository.save(station);

    }

    /**
     * Delete a station
     *
     * @param stationId station id
     */
    public void deleteStation(String stationId) {
        Station station = stationRepository.findOne(Example.of(Station.builder().id(stationId).build()))
                .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));
        stationRepository.delete(station);
    }
}
