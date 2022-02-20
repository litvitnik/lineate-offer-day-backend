package com.example.offerdaysongs;

import com.example.offerdaysongs.dto.requests.CreateCompanyRequest;
import com.example.offerdaysongs.dto.requests.CreateCopyrightRequest;
import com.example.offerdaysongs.dto.requests.CreateRecordingRequest;
import com.example.offerdaysongs.dto.requests.CreateSingerRequest;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Singer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.type.TimestampType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OfferDaySongsApplicationTests {

    public static final String singersUri = "/api/singers/";
    public static final String companiesUri = "/api/companies/";
    public static final String recordingsUri = "/api/recordings/";
    public static final String copyrightsUri = "/api/copyrights/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {

    }

    @Test
    public void createSinger() throws Exception{
        CreateSingerRequest singerRequest = new CreateSingerRequest();
        singerRequest.setName("Feduk");
        mockMvc
                .perform(post(singersUri).content(asJsonString(singerRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc
                .perform(get(singersUri + "/5"))
                .andExpect(content().json("{\"name\":\"Feduk\"}"));
    }

    @Test
    public void createCompany() throws Exception{
        CreateCompanyRequest companyRequest = new CreateCompanyRequest();
        companyRequest.setName("IBM");
        mockMvc
                .perform(post(companiesUri).content(asJsonString(companyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc
                .perform(get(companiesUri + "/5"))
                .andExpect(content().json("{\"name\":\"IBM\"}"));
    }

    @Test
    public void createRecordingWhenSingerExists() throws Exception{
        CreateRecordingRequest recordingRequest = new CreateRecordingRequest();
        recordingRequest.setTitle("TestSongName");
        Singer fakeSinger = new Singer();
        fakeSinger.setId(1L);
        recordingRequest.setSinger(fakeSinger);
        mockMvc
                .perform(post(recordingsUri).content(asJsonString(recordingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc
                .perform(get(recordingsUri + "/5"))
                .andExpect(content().json("{\"title\":\"TestSongName\"}"));
    }

    @Test
    public void createRecordingWhenSingerIsAbsent() throws Exception{
        CreateRecordingRequest recordingRequest = new CreateRecordingRequest();
        recordingRequest.setTitle("TestSongName");
        Singer fakeSinger = new Singer();
        fakeSinger.setId(5L);
        fakeSinger.setName("Abuba");
        recordingRequest.setSinger(fakeSinger);
        mockMvc
                .perform(post(recordingsUri).content(asJsonString(recordingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc
                .perform(get(recordingsUri + "/5"))
                .andExpect(content().json("{\"title\":\"TestSongName\"}"));
        mockMvc
                .perform(get(singersUri + "/5"))
                .andExpect(content().json("{\"name\":\"Abuba\"}"));
    }

    @Test
    public void copyrightIntegrational() throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV");
        ZonedDateTime begins = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        String beginsString =  ZonedDateTime.ofInstant(begins.toInstant(), ZoneOffset.UTC).format(formatter).trim();
        ZonedDateTime expires = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        String expiresString =  ZonedDateTime.ofInstant(expires.toInstant(), ZoneOffset.UTC).format(formatter).trim();
        CreateCopyrightRequest copyrightRequest = new CreateCopyrightRequest();
        copyrightRequest.setFee(BigDecimal.TEN);
        Company company = new Company();
        company.setId(1L);
        Recording recording = new Recording();
        recording.setId(1L);
        copyrightRequest.setCompany(company);
        copyrightRequest.setRecording(recording);
        copyrightRequest.setBegins(begins);
        copyrightRequest.setExpires(expires);
        mockMvc
                .perform(post(copyrightsUri).content(asJsonString(copyrightRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc
                .perform(post(copyrightsUri).content(asJsonString(copyrightRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //Проверка на корректную дату в создании
        mockMvc
                .perform(get(copyrightsUri + "/1"))
                .andExpect(content().json("{\"begins\":\"" + beginsString + "\"}"))
                .andExpect(content().json("{\"expires\":\"" + expiresString + "\"}"));
        //Поиск по периоду
        //TODO проверочку сюда
        mockMvc
                .perform(get(copyrightsUri + "period/")
                        .param("begins", beginsString)
                        .param("expires", expiresString))
                .andDo(print());

        //Получение стоимости
        mockMvc
                .perform(get(recordingsUri + "/1/fee/"))
                .andDo(print());
        //Получение стоимости использования песни
    }




    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper = mapper.findAndRegisterModules();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
