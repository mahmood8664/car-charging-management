package fi.develon.ev.tdd;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.ev.entity.Company;
import fi.develon.ev.entity.Station;
import fi.develon.ev.model.BaseResponse;
import fi.develon.ev.model.CreateStationRequest;
import fi.develon.ev.model.PagingResponse;
import fi.develon.ev.model.StationDto;
import fi.develon.ev.testcontainer.MongoDBIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author mahmood
 * @since 9/11/21
 */
public class StationCrudTest extends MongoDBIT {

    @Test
    void getAllStationsTest_OK() throws Exception {
        addCompaniesAndStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stations?pageNumber=0&size=4"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<PagingResponse<StationDto>> allStationsResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(allStationsResponse.isSuccessful()).isTrue();
        Assertions.assertThat(allStationsResponse.getResponse().getResponseList()).isNotNull();
        Assertions.assertThat(allStationsResponse.getResponse().getResponseList().size()).isEqualTo(4);
        Assertions.assertThat(allStationsResponse.getResponse().isHasNext()).isFalse();

        allStationsResponse.getResponse().getResponseList().forEach(stationDto -> {
            if (stationDto.getStationName().equals("S11")) {
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(11))).isEqualTo(0);
                Assertions.assertThat(stationDto.getLongitude().compareTo(BigDecimal.valueOf(11))).isEqualTo(0);
            }

            if (stationDto.getStationName().equals("S12")) {
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(12))).isEqualTo(0);
                Assertions.assertThat(stationDto.getLongitude().compareTo(BigDecimal.valueOf(12))).isEqualTo(0);
            }

            if (stationDto.getStationName().equals("S21")) {
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(21))).isEqualTo(0);
                Assertions.assertThat(stationDto.getLongitude().compareTo(BigDecimal.valueOf(21))).isEqualTo(0);
            }

            if (stationDto.getStationName().equals("S22")) {
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(22))).isEqualTo(0);
                Assertions.assertThat(stationDto.getLongitude().compareTo(BigDecimal.valueOf(22))).isEqualTo(0);
            }
        });
    }

    @Test
    void getAllStationsOfCompanyTest_OK() throws Exception {
        addCompaniesAndStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stations?pageNumber=0&size=4&companyId=1111"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<PagingResponse<StationDto>> allStationsResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(allStationsResponse.isSuccessful()).isTrue();
        Assertions.assertThat(allStationsResponse.getResponse().getResponseList()).isNotNull();
        Assertions.assertThat(allStationsResponse.getResponse().getResponseList().size()).isEqualTo(2);
        Assertions.assertThat(allStationsResponse.getResponse().isHasNext()).isFalse();

        allStationsResponse.getResponse().getResponseList().forEach(stationDto -> {
            if (stationDto.getStationName().equals("S11")) {
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(11))).isEqualTo(0);
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(11))).isEqualTo(0);
            }

            if (stationDto.getStationName().equals("S12")) {
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(12))).isEqualTo(0);
                Assertions.assertThat(stationDto.getLatitude().compareTo(BigDecimal.valueOf(12))).isEqualTo(0);
            }
        });
    }

    @Test
    void getAllStationsOfCompanyTest_NotResult() throws Exception {
        addCompaniesAndStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stations?pageNumber=0&size=4&companyId=12334"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<PagingResponse<StationDto>> allStationsResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });


        Assertions.assertThat(allStationsResponse.isSuccessful()).isTrue();
        Assertions.assertThat(allStationsResponse.getResponse().getResponseList()).isNotNull();
        Assertions.assertThat(allStationsResponse.getResponse().getResponseList().size()).isEqualTo(0);
        Assertions.assertThat(allStationsResponse.getResponse().isHasNext()).isFalse();
    }

    @Test
    void getStationTest_OK() throws Exception {
        addCompaniesAndStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stations/11111"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<StationDto> getCompanyResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(getCompanyResponse.isSuccessful()).isTrue();
        Assertions.assertThat(getCompanyResponse.getResponse()).isNotNull();
        Assertions.assertThat(getCompanyResponse.getResponse().getCompanyId()).isEqualTo("1111");
        Assertions.assertThat(getCompanyResponse.getResponse().getStationId()).isEqualTo("11111");
        Assertions.assertThat(getCompanyResponse.getResponse().getStationName()).isEqualTo("S11");
        Assertions.assertThat(getCompanyResponse.getResponse().getLatitude().compareTo(BigDecimal.valueOf(11))).isEqualTo(0);
        Assertions.assertThat(getCompanyResponse.getResponse().getLongitude().compareTo(BigDecimal.valueOf(11))).isEqualTo(0);
    }

    @Test
    void getStationTest_NotFound() throws Exception {
        addCompaniesAndStations();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stations/1233"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createStationTest_OK() throws Exception {
        addCompaniesAndStations();

        CreateStationRequest request = CreateStationRequest.builder()
                .companyId("1111")
                .stationName("S13")
                .latitude(BigDecimal.valueOf(15))
                .longitude(BigDecimal.valueOf(16))
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<String> createResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(createResponse.isSuccessful()).isTrue();
        Assertions.assertThat(createResponse.getResponse()).isNotEmpty();

        Optional<Station> stationOptional = stationRepository.findOne(Example.of(Station.builder().id(createResponse.getResponse()).build()));
        stationOptional.ifPresentOrElse(st -> {
            Assertions.assertThat(st.getCompanyId()).isEqualTo(request.getCompanyId());
            Assertions.assertThat(st.getName()).isEqualTo(request.getStationName());
            Assertions.assertThat(st.getLocation().getY()).isEqualTo(request.getLatitude().doubleValue());
            Assertions.assertThat(st.getLocation().getX()).isEqualTo(request.getLongitude().doubleValue());
        }, () -> Assertions.fail("station is not created!"));

    }


    @Test
    void createStationTest_NotFound() throws Exception {
        addCompaniesAndStations();

        CreateStationRequest request = CreateStationRequest.builder()
                .companyId("123123")
                .stationName("S13")
                .latitude(BigDecimal.valueOf(15))
                .longitude(BigDecimal.valueOf(16))
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        BaseResponse<Void> createResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(createResponse.isSuccessful()).isFalse();
        Assertions.assertThat(createResponse.getErrorDto().getErrorCode()).isEqualTo(1002);

    }


    @Test
    void updateStationTest_OK() throws Exception {
        addCompaniesAndStations();

        StationDto request = StationDto.builder()
                .stationId("11111")
                .stationName("new")
                .companyId("2222")
                .latitude(BigDecimal.valueOf(50))
                .longitude(BigDecimal.valueOf(60))
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isTrue();

        Optional<Station> stationOptional = stationRepository.findOne(Example.of(Station.builder().id("11111").build()));
        stationOptional.ifPresentOrElse(station -> {
            Assertions.assertThat(station.getCompanyId()).isEqualTo(request.getCompanyId());
            Assertions.assertThat(station.getName()).isEqualTo(request.getStationName());
            Assertions.assertThat(station.getLocation().getY()).isEqualTo(request.getLatitude().doubleValue());
            Assertions.assertThat(station.getLocation().getX()).isEqualTo(request.getLongitude().doubleValue());
        }, () -> Assertions.fail("station update is failed!"));

    }

    @Test
    void updateStationTest_StationNotFound() throws Exception {
        addCompaniesAndStations();

        StationDto request = StationDto.builder()
                .stationId("123123")
                .stationName("new")
                .companyId("2222")
                .latitude(BigDecimal.valueOf(50))
                .longitude(BigDecimal.valueOf(60))
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isFalse();
    }

    @Test
    void updateStationTest_CompanyNotFound() throws Exception {
        addCompaniesAndStations();

        StationDto request = StationDto.builder()
                .stationId("11111")
                .stationName("new")
                .companyId("12321")
                .latitude(BigDecimal.valueOf(50))
                .longitude(BigDecimal.valueOf(60))
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isFalse();
    }


    @Test
    void deleteStationTest_OK() throws Exception {
        addCompaniesAndStations();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/stations/11111")
        ).andExpect(MockMvcResultMatchers.status().isOk());

        BaseResponse<Void> updateResponse = json.readValue(
                resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
                });

        Assertions.assertThat(updateResponse.isSuccessful()).isTrue();

        Optional<Station> stationOptional = stationRepository.findOne(Example.of(Station.builder().id("11111").build()));
        stationOptional.ifPresent(station -> Assertions.fail("deletion is not successful"));

    }

    @Test
    void deleteStationTest_NotFound() throws Exception {
        addCompaniesAndStations();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/stations/sdf34")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private void addCompaniesAndStations() {
        Company company1 = companyRepository.save(
                Company.builder()
                        .id("1111")
                        .name("111")
                        .build());

        Company company2 = companyRepository.save(
                Company.builder()
                        .id("2222")
                        .name("222")
                        .build());

        stationRepository.saveAll(List.of(Station.builder()
                        .id("11111")
                        .companyId(company1.getId())
                        .location(new GeoJsonPoint(11, 11))
                        .name("S11")
                        .build(),
                Station.builder()
                        .id("22222")
                        .companyId(company1.getId())
                        .location(new GeoJsonPoint(12, 12))
                        .name("S12")
                        .build(),
                Station.builder()
                        .id("33333")
                        .companyId(company2.getId())
                        .location(new GeoJsonPoint(21, 21))
                        .name("S21")
                        .build(),
                Station.builder()
                        .id("44444")
                        .companyId(company2.getId())
                        .location(new GeoJsonPoint(22, 22))
                        .name("S22")
                        .build()));
    }

}
