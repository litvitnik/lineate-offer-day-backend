package com.example.offerdaysongs;

import com.example.offerdaysongs.dto.requests.CreateCompanyRequest;
import com.example.offerdaysongs.dto.requests.CreateCopyrightRequest;
import com.example.offerdaysongs.dto.requests.CreateRecordingRequest;
import com.example.offerdaysongs.dto.requests.CreateSingerRequest;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.model.Singer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    public void singerIntegrationTest() throws Exception{
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
    public void companyIntegrationTest() throws Exception{
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
    public void recordingIntegrationTest() throws Exception{
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
        CreateRecordingRequest recordingRequest2 = new CreateRecordingRequest();
        recordingRequest2.setTitle("TestSongName2");
        Singer fakeSinger2 = new Singer();
        fakeSinger2.setId(5L);
        fakeSinger2.setName("Abuba");
        recordingRequest2.setSinger(fakeSinger2);
        mockMvc
                .perform(post(recordingsUri).content(asJsonString(recordingRequest2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc
                .perform(get(recordingsUri + "/6"))
                .andExpect(content().json("{\"title\":\"TestSongName2\"}"));

        mockMvc
                .perform(get(singersUri))
                .andDo(print())
                .andExpect(jsonPath("$[*].name").value(hasItem("Abuba")));
    }


    @Test
    public void copyrightIntegrationTest() throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV");
        ZonedDateTime begins = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        String beginsString =  ZonedDateTime.ofInstant(begins.toInstant(), ZoneOffset.UTC).format(formatter).trim();
        ZonedDateTime expires = ZonedDateTime.ofInstant(Instant.ofEpochSecond(1695469871), ZoneOffset.UTC);
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

        ZonedDateTime expiredAlready = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        copyrightRequest.setExpires(expiredAlready);

        mockMvc
                .perform(post(copyrightsUri).content(asJsonString(copyrightRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc
                .perform(get(copyrightsUri + "/1"))
                .andExpect(content().json("{\"begins\":\"" + beginsString + "\"}"))
                .andExpect(content().json("{\"expires\":\"" + expiresString + "\"}"));

        mockMvc
                .perform(get(copyrightsUri + "period/")
                        .param("begins", beginsString)
                        .param("expires", expiresString))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        //Получение стоимости
        mockMvc
                .perform(get(recordingsUri + "/1/fee/"))
                .andExpect(content().string("20"));

        mockMvc
                .perform(get(recordingsUri + "/2/fee/"))
                .andExpect(content().string("0"));

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
