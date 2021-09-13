package fi.develon.ev.tdd;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.Station;
import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.CompanyDetailDto;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * @author mahmood
 * @since 9/11/21
 */
class CompanyDetailsTest extends MongoDBIT {

    @Test
    void getCompanyDetailTest_OK() throws Exception {
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
        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getParentCompanyId()).isNull();

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
        Assertions.assertThat(companyDetailDto2.getStations().size()).isZero();
        Assertions.assertThat(companyDetailDto2.getChildCompanies().size()).isZero();
    }

    @Test
    void getCompanyDetailNotIncludeChildrenTest_OK() throws Exception {
        addThreeCompanies();
        addStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/companies/1111/details?include_children=false"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<CompanyDetailDto> companyDetailDto = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(companyDetailDto.isSuccessful()).isTrue();

        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getCompanyId()).isEqualTo("1111");
        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getCompanyName()).isEqualTo("111");
        Assertions.assertThat(companyDetailDto.getResponse().getCompany().getParentCompanyId()).isNull();

        Assertions.assertThat(companyDetailDto.getResponse().getStations().size()).isEqualTo(2);
        Assertions.assertThat(companyDetailDto.getResponse().getStations().get(0).getStationId()).isEqualTo("11111");
        Assertions.assertThat(companyDetailDto.getResponse().getStations().get(1).getStationId()).isEqualTo("22222");

        Assertions.assertThat(companyDetailDto.getResponse().getChildCompanies().size()).isZero();

    }


    @Test
    void getCompanyDetailSubTree_OK() throws Exception {
        addThreeCompanies();
        addStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/companies/2222/details?include_children=true"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<CompanyDetailDto> companyDetailDto = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(companyDetailDto.isSuccessful()).isTrue();

        //2222
        CompanyDetailDto companyDetailDto1 = companyDetailDto.getResponse();
        Assertions.assertThat(companyDetailDto1.getCompany().getCompanyId()).isEqualTo("2222");
        Assertions.assertThat(companyDetailDto1.getStations().size()).isEqualTo(2);
        Assertions.assertThat(companyDetailDto1.getStations().get(0).getStationId()).isEqualTo("33333");
        Assertions.assertThat(companyDetailDto1.getStations().get(1).getStationId()).isEqualTo("44444");

        //3333
        List<CompanyDetailDto> childCompanies = companyDetailDto1.getChildCompanies();
        Assertions.assertThat(childCompanies.size()).isEqualTo(1);
        CompanyDetailDto companyDetailDto2 = childCompanies.get(0);
        Assertions.assertThat(companyDetailDto2.getCompany().getCompanyId()).isEqualTo("3333");
        Assertions.assertThat(companyDetailDto2.getStations().size()).isZero();
        Assertions.assertThat(companyDetailDto2.getChildCompanies().size()).isZero();
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
