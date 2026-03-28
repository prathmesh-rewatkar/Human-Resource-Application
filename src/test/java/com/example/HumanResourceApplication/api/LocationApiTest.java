package com.example.HumanResourceApplication.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationApiTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetAllLocations() throws Exception {
        mockMvc.perform(get("/locations")) //mocks the api endpoint
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.locations").isArray());
    }

    @Test
    public void testGetAllLocations_WhenEmpty() throws Exception {
        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.locations").exists())
                .andExpect(jsonPath("$._embedded.locations").isArray())
                .andExpect(jsonPath("$._embedded.locations.length()").value(0));
    }


}