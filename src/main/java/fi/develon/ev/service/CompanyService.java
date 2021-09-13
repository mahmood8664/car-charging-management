package fi.develon.ev.service;

import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.CompanyTree;
import fi.develon.ev.entity.Station;
import fi.develon.ev.exception.SMException;
import fi.develon.ev.exception.SMExceptionType;
import fi.develon.ev.model.*;
import fi.develon.ev.repository.CompanyDetialsRepository;
import fi.develon.ev.repository.CompanyRepository;
import fi.develon.ev.repository.StationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Company's services
 *
 * @author mahmood
 * @since 9/10/21
 */
@Service
@Slf4j
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final StationRepository stationRepository;
    private final CompanyDetialsRepository companyDetialsRepository;

    /**
     * Returns all companies, pagination is mandatory
     *
     * @param request pagination request
     * @return {@link PagingResponse}
     */
    public PagingResponse<CompanyDto> allCompanies(PaginationRequest request) {
        Slice<Company> allCompanies = companyRepository.findAll(Example.of(Company.builder().build()),
                PageRequest.of(request.getPageNumber(), request.getSize()));
        return PagingResponse.of(allCompanies.get().map(DtoMapper::getCompanyDto).collect(Collectors.toList()),
                allCompanies.hasNext());
    }

    /**
     * Return company info by companyId
     *
     * @param companyId company id
     * @return {@link CompanyDto}
     */
    public CompanyDto getCompany(String companyId) {
        return companyRepository.findOne(Example.of(Company.builder().id(companyId).build()))
                .map(DtoMapper::getCompanyDto)
                .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));
    }

    /**
     * Create new company
     *
     * @param request {@link CreateCompanyRequest}
     * @return created company id
     */
    public String createCompany(CreateCompanyRequest request) {
        String s = null;
        if (StringUtils.isNotEmpty(request.getParentCompanyId())) {
            s = companyRepository.findOne(Example.of(Company.builder()
                    .id(request.getParentCompanyId())
                    .build())).orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND)).getId();

        }
        return companyRepository.save(Company.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getCompanyName())
                .parentCompanyId(s)
                .build()).getId();
    }

    /**
     * update company
     *
     * @param request Company to update
     */
    public void updateCompany(CompanyDto request) {
        Optional<Company> company = companyRepository.findOne(Example.of(Company.builder()
                .id(request.getCompanyId())
                .build()));

        if (StringUtils.isNotBlank(request.getParentCompanyId())) {
            companyRepository.findOne(Example.of(Company.builder().id(request.getParentCompanyId()).build()))
                    .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));
        }

        company.ifPresent(cmp -> {
            cmp.setName(request.getCompanyName());
            cmp.setParentCompanyId(request.getParentCompanyId());

            if (cmp.getParentCompanyId() != null && cmp.getParentCompanyId().equals(cmp.getId())) {
                throw new SMException(SMExceptionType.BAD_REQUEST, "company can be it's parent!");
            }

            companyRepository.save(cmp);
        });

        company.orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));
    }

    /**
     * Delete company
     *
     * @param companyId company id to delete
     */
    public void deleteCompany(String companyId) {

        Company company = companyRepository.findOne(Example.of(Company.builder().id(companyId).build()))
                .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));

        List<Company> children = companyRepository.findAll(Example.of(Company.builder().parentCompanyId(company.getId()).build()));
        Company parent = companyRepository.findOne(Example.of(Company.builder().id(company.getParentCompanyId()).build())).orElse(null);
        children.forEach(cmp -> cmp.setParentCompanyId(parent == null ? null : parent.getId()));

        companyRepository.saveAll(children);
        companyRepository.delete(company);

        List<Station> stations = stationRepository.findAll(Example.of(Station.builder().companyId(companyId).build()));
        stationRepository.deleteAll(stations);

    }

    /**
     * Get company details with all children companies and their stations
     *
     * @param companyId        given company id
     * @param include_children should include children company or not
     * @return {@link CompanyDetailDto}
     */
    public CompanyDetailDto getCompanyDetails(String companyId, boolean include_children) {

        if (include_children) {
            Optional<CompanyTree> companyFlatTree = companyDetialsRepository.getCompanyFlatTree(companyId, 100L);
            CompanyTree companyTree = companyFlatTree.orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));

            List<String> companyIds = new ArrayList<>();
            companyIds.add(companyTree.getId());
            companyIds.addAll(companyTree.getChildCompanies().stream().map(Company::getId).collect(Collectors.toList()));
            List<Station> stationList = stationRepository.findAllByCompanyIdIn(companyIds);

            return DtoMapper.toCompanyDetailDto(companyTree, stationList);

        } else {
            Company company = companyRepository.findOne(Example.of(Company.builder().id(companyId).build()))
                    .orElseThrow(() -> new SMException(SMExceptionType.NOT_FOUND));
            List<Station> stations = stationRepository.findAll(Example.of(Station.builder().companyId(companyId).build()));
            return CompanyDetailDto.builder()
                    .company(DtoMapper.getCompanyDto(company))
                    .stations(stations.stream().map(DtoMapper::getStationDto).collect(Collectors.toList()))
                    .childCompanies(Collections.emptyList())
                    .build();
        }
    }
}
