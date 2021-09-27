package fi.develon.ev.service;

import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.CompanyTree;
import fi.develon.ev.entity.Station;
import fi.develon.ev.exception.SMException;
import fi.develon.ev.exception.SMExceptionType;
import fi.develon.ev.model.*;
import fi.develon.ev.repository.CompanyDetailsRepository;
import fi.develon.ev.repository.CompanyRepository;
import fi.develon.ev.repository.StationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private final CompanyDetailsRepository companyDetailsRepository;

    /**
     * find station by given creteria, this service is paginated.
     *
     * @param request {@link PaginationRequest}
     * @return {@link PagingResponse<StationDto>}
     */
    public PagingResponse<StationDto> findStations(PaginationRequest request) {

        Page<Station> stations = stationRepository.findAll(Example.of(Station.builder()
                .build()), PageRequest.of(request.getPageNumber(), request.getSize()));

        return PagingResponse.of(stations.get().map(DtoMapper::getStationDto).collect(Collectors.toList()), stations.hasNext());
    }

    public List<StationDto> findStationsOfCompany(String companyId) {

        Optional<CompanyTree> companyFlatTree = companyDetailsRepository.getCompanyFlatTree(companyId, 100L);
        List<Station> stations = companyFlatTree.map(companyTree -> {
            List<String> companyIds = new ArrayList<>();
            companyIds.add(companyTree.getId());
            companyIds.addAll(companyTree.getChildCompanies().stream().map(Company::getId).collect(Collectors.toList()));
            return stationRepository.findAllByCompanyIdIn(companyIds);
        }).orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));

        return stations.stream().map(DtoMapper::getStationDto).collect(Collectors.toList());
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
                .id(UUID.randomUUID().toString())
                .name(request.getStationName())
                .location(new GeoJsonPoint(request.getLongitude().doubleValue(), request.getLatitude().doubleValue()))
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

        station.setLocation(new GeoJsonPoint(request.getLongitude().doubleValue(), request.getLatitude().doubleValue()));
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

    public PagingResponse<StationDto> nearStations(NearbyStationsInquiryRequest request) {
        Slice<Station> byLocationNear = stationRepository.findByLocationNear(
                new GeoJsonPoint(request.getLongitude().doubleValue(), request.getLatitude().doubleValue()),
                new Distance(request.getDistance(), Metrics.KILOMETERS),
                PageRequest.of(request.getPageNumber(), request.getSize()));

        return PagingResponse.of(
                byLocationNear.getContent().stream().map(DtoMapper::getStationDto).collect(Collectors.toList()),
                byLocationNear.hasNext()
        );
    }
}
