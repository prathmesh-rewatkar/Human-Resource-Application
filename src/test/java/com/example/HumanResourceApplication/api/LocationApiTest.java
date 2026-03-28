package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.entity.Country;
import com.example.HumanResourceApplication.entity.Location;
import com.example.HumanResourceApplication.exception.ResourceNotFoundException;
import com.example.HumanResourceApplication.repository.CountryRepository;
import com.example.HumanResourceApplication.repository.LocationRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    private LocationRepository locationRepository ;


    @Test
    public void testGetAllLocations() throws Exception {
        mockMvc.perform(get("/locations")) //mocks the api endpoint
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.locations").isArray());
    }




}