package com.example.offerdaysongs;

import com.example.offerdaysongs.dto.requests.CreateCompanyRequest;
import com.example.offerdaysongs.dto.requests.CreateRecordingRequest;
import com.example.offerdaysongs.dto.requests.CreateSingerRequest;
import com.example.offerdaysongs.model.Singer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OfferDaySongsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {

    }

    @Test
    public void createSinger() throws Exception{
        String uri = "/api/singers/";
        CreateSingerRequest singerRequest = new CreateSingerRequest();
        singerRequest.setName("Feduk");
        MvcResult mvcResult = mockMvc
                .perform(post(uri).content(asJsonString(singerRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc
                .perform(get(uri + "/5"))
                .andExpect(content().json("{\"name\":\"Feduk\"}"));
    }

    @Test
    public void createCompany() throws Exception{
        String uri = "/api/companies/";
        CreateCompanyRequest companyRequest = new CreateCompanyRequest();
        companyRequest.setName("IBM");
        MvcResult mvcResult = mockMvc
                .perform(post(uri).content(asJsonString(companyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc
                .perform(get(uri + "/5"))
                .andExpect(content().json("{\"name\":\"IBM\"}"));
    }
    //TODO
    @Disabled
    @Test
    public void createRecording() throws Exception{
        String uri = "/api/companies/";
        CreateRecordingRequest recordingRequest = new CreateRecordingRequest();
        Singer fakeSinger = new Singer();
        fakeSinger.setId(1L);
        recordingRequest.setSinger(fakeSinger);
        MvcResult mvcResult = mockMvc
                .perform(post(uri).content(asJsonString(recordingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc
                .perform(get(uri + "/5"))
                .andExpect(content().json("{\"name\":\"IBM\"}"));
    }
    //TODO
    @Disabled
    @Test
    public void createCopyright() throws Exception{
        String uri = "/api/companies/";
        CreateCompanyRequest companyRequest = new CreateCompanyRequest();
        companyRequest.setName("IBM");
        MvcResult mvcResult = mockMvc
                .perform(post(uri).content(asJsonString(companyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc
                .perform(get(uri + "/5"))
                .andExpect(content().json("{\"name\":\"IBM\"}"));
    }




    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
