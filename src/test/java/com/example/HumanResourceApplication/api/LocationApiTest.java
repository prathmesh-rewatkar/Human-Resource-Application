package com.example.HumanResourceApplication.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LocationApiTest {

    @Autowired
    private MockMvc mockMvc;

    // ================= GET =================
    @Test
    public void testGetAllLocations() throws Exception {
        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.locations").isArray());
    }

    // ================= PUT =================
    @Test
    @Transactional
    public void testUpdateLocation_PUT_Success() throws Exception {
        String requestBody = """
            {
                "streetAddress": "Updated Street 99",
                "postalCode": "00989",
                "city": "Roma",
                "stateProvince": "Lazio",
                "country": "http://localhost/countries/IT"
            }
            """;

        mockMvc.perform(put("/locations/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    // PUT = UPSERT → creates new
    @Test
    @Transactional
    public void testUpdateLocation_PUT_NotFound() throws Exception {
        String requestBody = """
            {
                "streetAddress": "Ghost Street",
                "postalCode": "00000",
                "city": "Nowhere",
                "stateProvince": "None",
                "country": "http://localhost/countries/IT"
            }
            """;

        mockMvc.perform(put("/locations/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated()); //
    }

    // ================= PATCH =================
    @Test
    @Transactional
    public void testUpdateLocation_PATCH_Success() throws Exception {
        String requestBody = """
            {
                "city": "Milan",
                "stateProvince": "Lombardy"
            }
            """;

        mockMvc.perform(patch("/locations/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    //  NOW RETURNS 404 (your current backend behavior)
    @Test
    public void testUpdateLocation_PATCH_NotFound() throws Exception {
        String requestBody = """
            {
                "city": "Nowhere"
            }
            """;

        mockMvc.perform(patch("/locations/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    // ================= DELETE =================
    @Test
    @Transactional
    public void testDeleteLocation_Success() throws Exception {

        String createBody = """
        {
            "locationId": 5000,
            "streetAddress": "Test",
            "postalCode": "12345",
            "city": "TestCity",
            "stateProvince": "TestState",
            "country": "http://localhost/countries/IN"
        }
        """;

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/locations/5000"))
                .andExpect(status().isNoContent());
    }

    // ✅ NOW RETURNS 404 (your backend behavior)
    @Test
    @Transactional
    public void testDeleteLocation_NotFound() throws Exception {
        mockMvc.perform(delete("/locations/9999"))
                .andExpect(status().isNotFound());
    }

    // ================= POST =================
    @Test
    @Transactional
    public void testAddLocation_Success() throws Exception {
        String requestBody = """
            {
                "locationId": 6000,
                "streetAddress": "123 Test Street",
                "postalCode": "411001",
                "city": "Nagpur",
                "stateProvince": "Maharashtra",
                "country": "http://localhost/countries/IN"
            }
            """;

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Nagpur"))
                .andExpect(jsonPath("$.stateProvince").value("Maharashtra"));
    }
}