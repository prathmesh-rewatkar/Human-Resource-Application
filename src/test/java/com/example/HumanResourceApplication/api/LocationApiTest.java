package com.example.HumanResourceApplication.api;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

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
    @Transactional
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
                .andExpect(status().isBadRequest()); //
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
    @Transactional
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
        "streetAddress": "Test",
        "postalCode": "12345",
        "city": "TestCity",
        "stateProvince": "TestState",
        "country": "http://localhost/countries/IN"
    }
    """;

        // Capture the Location header from the create response
        MvcResult result = mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the ID from the Location header (e.g., "http://localhost/locations/42")
        String locationHeader = result.getResponse().getHeader("Location");
        String id = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        mockMvc.perform(delete("/locations/" + id))
                .andExpect(status().isNoContent());
    }

    // NOW RETURNS 404
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

    // TEST: Add location with non-existing country → should fail
    @Test
    @Transactional
    public void testAddLocation_WithNonExistingCountry_Fails() throws Exception {
        String requestBody = """
            {
                "streetAddress": "123 Test Street",
                "postalCode": "411001",
                "city": "Nagpur",
                "stateProvince": "Maharashtra",
                "country": "http://localhost/countries/ZZ"
            }
            """;

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }

    //=======2nd page => get list of employees by department
    @Test
    @Transactional
    public void testGetAllDepartmentWiseEmployeesByLocation() throws Exception {

        // STEP 1: Get all departments for location 1700
        MvcResult result = mockMvc.perform(get("/locations/1700/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").isArray())
                .andReturn();

        // STEP 2: Extract ALL employees hrefs directly from response
        String responseBody = result.getResponse().getContentAsString();
        List<String> employeeHrefs = JsonPath.read(responseBody,
                "$._embedded.departments[*]._links.employees.href");

        // STEP 3: Loop through each employees href
        for (String href : employeeHrefs) {

            // strip templated part {?projection} if present
            String cleanHref = href.replace("{?projection}", "");

            // extract path after localhost e.g. "/department/10/employees"
            String path = cleanHref.replace("http://localhost", "");

            mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded.employees").isArray());

            System.out.println(" " + path + " → employees verified");
        }
    }
}