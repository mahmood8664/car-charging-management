package fi.develon.ev.tdd;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.Station;
import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.CompanyDetailDto;
import fi.develon.ev.model.CompanyDto;
import fi.develon.ev.model.CreateCompanyRequest;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

/**
 * @author mahmood
 * @since 9/11/21
 */
public class CompanyDetailsTest extends MongoDBIT {

    @Test
    void getAllCompaniesTest_OK() throws Exception {
        addThreeCompanies();
        addStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/companies/1111/details?include_children=true"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<CompanyDetailDto> companyDetailDto = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(companyDetailDto.isSuccessful()).isTrue();

        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getCompanyId()).isEqualTo("1111");
        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getCompanyName()).isEqualTo("111");
        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getParentCompanyId()).isEqualTo(null);

        Assertions.assertThat(companyDetailDto.getResponse().getStations().size()).isEqualTo(2);
        Assertions.assertThat(companyDetailDto.getResponse().getStations().get(0).getStationId()).isEqualTo("11111");
        Assertions.assertThat(companyDetailDto.getResponse().getStations().get(1).getStationId()).isEqualTo("22222");

        Assertions.assertThat(companyDetailDto.getResponse().getChildCompanies().size()).isEqualTo(1);

        //2222
        CompanyDetailDto companyDetailDto1 = companyDetailDto.getResponse().getChildCompanies().get(0);
        Assertions.assertThat(companyDetailDto1.getCompany().getCompanyId()).isEqualTo("2222");
        Assertions.assertThat(companyDetailDto1.getStations().size()).isEqualTo(2);
        Assertions.assertThat(companyDetailDto1.getStations().get(0).getStationId()).isEqualTo("33333");
        Assertions.assertThat(companyDetailDto1.getStations().get(1).getStationId()).isEqualTo("44444");

        //3333
        List<CompanyDetailDto> childCompanies = companyDetailDto1.getChildCompanies();
        Assertions.assertThat(childCompanies.size()).isEqualTo(1);
        CompanyDetailDto companyDetailDto2 = childCompanies.get(0);
        Assertions.assertThat(companyDetailDto2.getCompany().getCompanyId()).isEqualTo("3333");
        Assertions.assertThat(companyDetailDto2.getStations().size()).isEqualTo(0);
        Assertions.assertThat( companyDetailDto2.getChildCompanies()).isNull();
    }

    @Test
    void getCompanyTest_OK() throws Exception {
        addTwoCompanies();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/companies/2222"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<CompanyDto> getCompanyResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(getCompanyResponse.isSuccessful()).isTrue();
        Assertions.assertThat(getCompanyResponse.getResponse()).isNotNull();
        Assertions.assertThat(getCompanyResponse.getResponse().getParentCompanyId()).isEqualTo("1111");
        Assertions.assertThat(getCompanyResponse.getResponse().getCompanyName()).isEqualTo("222");
        Assertions.assertThat(getCompanyResponse.getResponse().getParentCompanyId()).isEqualTo("1111");

    }

    @Test
    void getCompanyTest_NotFound() throws Exception {
        addTwoCompanies();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/companies/3333"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void createCompanyTest_OK() throws Exception {
        addTwoCompanies();

        CreateCompanyRequest request = CreateCompanyRequest.builder()
                .companyName("3333")
                .parentCompanyId("2222")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<String> createResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(createResponse.isSuccessful()).isTrue();
        Assertions.assertThat(createResponse.getResponse()).isNotEmpty();

        Optional<Company> cmp = companyRepository.findOne(Example.of(Company.builder().id(createResponse.getResponse()).build()));
        cmp.ifPresentOrElse(company -> {
            Assertions.assertThat(company.getParentCompanyId()).isEqualTo(request.getParentCompanyId());
            Assertions.assertThat(company.getName()).isEqualTo(request.getCompanyName());
        }, () -> Assertions.fail("company is not created!"));

    }

    @Test
    void createCompanyTest_NotFound() throws Exception {
        addTwoCompanies();

        CreateCompanyRequest request = CreateCompanyRequest.builder()
                .companyName("3333")
                .parentCompanyId("sdfds")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    void updateCompanyTest_OK() throws Exception {
        addThreeCompanies();

        CompanyDto request = CompanyDto.builder()
                .companyId("3333")
                .companyName("new")
                .parentCompanyId("1111")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isTrue();

        Optional<Company> cmp = companyRepository.findOne(Example.of(Company.builder().id("3333").build()));
        cmp.ifPresentOrElse(company -> {
            Assertions.assertThat(company.getParentCompanyId()).isEqualTo(request.getParentCompanyId());
            Assertions.assertThat(company.getName()).isEqualTo(request.getCompanyName());
        }, () -> Assertions.fail("company update is failed!"));

    }

    @Test
    void updateCompanyTest_NotFound() throws Exception {
        addThreeCompanies();

        CompanyDto request = CompanyDto.builder()
                .companyId("3333")
                .companyName("new")
                .parentCompanyId("1111111")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    void deleteCompanyTest_OK() throws Exception {
        addThreeCompanies();
        addStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/companies/2222")
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isTrue();

        Optional<Company> cmp = companyRepository.findOne(Example.of(Company.builder().id("2222").build()));
        cmp.ifPresent(company -> Assertions.fail("deletion is not successful"));

        //Check that child companies of deleted company is belong to parent company of deleted company (if exist)
        Optional<Company> cmp2 = companyRepository.findOne(Example.of(Company.builder().id("3333").build()));
        cmp2.ifPresentOrElse(company -> Assertions.assertThat(company.getParentCompanyId()).isEqualTo("1111"),
                () -> Assertions.fail("cannot find company with id 1111"));

        stationRepository.findAll(Example.of(Station.builder().companyId("2222").build())).stream().findAny().ifPresent(station ->
                Assertions.fail("Station of company must be deleted after deletion of company"));
    }

    @Test
    void deleteCompanyTest_NotFound() throws Exception {
        addThreeCompanies();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/companies/3434")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private void addTwoCompanies() {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build()));
    }


    private void addThreeCompanies() {
        companyRepository.saveAll(List.of(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build(),
                Company.builder()
                        .id("2222")
                        .name("222")
                        .parentCompanyId("1111")
                        .build(),
                Company.builder()
                        .id("3333")
                        .name("333")
                        .parentCompanyId("2222")
                        .build()));
    }

    private void addStations() {
        stationRepository.saveAll(List.of(Station.builder()
                        .id("11111")
                        .companyId("1111")
                        .location(new GeoJsonPoint(11, 11))
                        .name("S11")
                        .build(),
                Station.builder()
                        .id("22222")
                        .companyId("1111")
                        .location(new GeoJsonPoint(12, 12))
                        .name("S12")
                        .build(),
                Station.builder()
                        .id("33333")
                        .companyId("2222")
                        .location(new GeoJsonPoint(21, 21))
                        .name("S21")
                        .build(),
                Station.builder()
                        .id("44444")
                        .companyId("2222")
                        .location(new GeoJsonPoint(22, 22))
                        .name("S22")
                        .build()));
    }
}
