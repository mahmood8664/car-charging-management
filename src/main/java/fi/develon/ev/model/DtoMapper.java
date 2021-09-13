package fi.develon.ev.model;

import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.CompanyTree;
import fi.develon.ev.entity.Station;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author mahmood
 * @since 9/13/21
 */
public final class DtoMapper {

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

    public static CompanyDetailDto toCompanyDetailDto(CompanyTree companyTree, List<Station> stations) {

        Map<String, List<CompanyTree>> companyMap = new HashMap<>();//map of parentCompanyId and List of company with specified parent
        Map<String, List<StationDto>> stationDtoMap = new HashMap<>();//map of companyId and it's stations list

        for (Station station : stations) {
            stationDtoMap.putIfAbsent(station.getCompanyId(), new ArrayList<>());
            stationDtoMap.get(station.getCompanyId()).add(getStationDto(station));
        }

        companyMap.put(null, List.of(companyTree));//parent
        for (CompanyTree childCompany : companyTree.getChildCompanies()) {
            companyMap.computeIfAbsent(childCompany.getParentCompanyId(), k -> new ArrayList<>());
            companyMap.get(childCompany.getParentCompanyId()).add(childCompany);
        }

        return CompanyDetailDto.builder()
                .company(getCompanyDto(companyTree))
                .stations(stationDtoMap.get(companyTree.getId()))
                .childCompanies(calculateChild(companyMap.get(companyTree.getId()), companyMap, stationDtoMap))
                .build();
    }

    private static List<CompanyDetailDto> calculateChild(List<CompanyTree> companyTrees, Map<String,
            List<CompanyTree>> companyMap, Map<String, List<StationDto>> stationDtoMap) {
        if (companyTrees == null || companyTrees.isEmpty()) {
            return Collections.emptyList();
        }

        List<CompanyDetailDto> companyDetailDtoList = new ArrayList<>();
        for (CompanyTree companyTree : companyTrees) {
            companyDetailDtoList.add(CompanyDetailDto.builder()
                    .company(DtoMapper.getCompanyDto(companyTree))
                    .stations(stationDtoMap.get(companyTree.getId()) != null ? stationDtoMap.get(companyTree.getId()) : Collections.emptyList())
                    .childCompanies(calculateChild(companyMap.get(companyTree.getId()), companyMap, stationDtoMap))
                    .build());
        }
        return companyDetailDtoList;
    }
}
