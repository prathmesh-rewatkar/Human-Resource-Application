package com.example.HumanResourceApplication.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET all employees API")
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees").exists());
    }

    @Test
    @DisplayName("GET all employees - validate structure")
    void testGetAllEmployees_Structure() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees").isArray());
    }

    @Test
    @DisplayName("GET all employees - should return non-empty list")
    void testGetAllEmployees_NotEmpty() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    @DisplayName("GET all employees - content type should be HAL JSON")
    void testGetAllEmployees_ContentType() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"));
    }

    @Test
    @DisplayName("GET all employees - response should not be empty")
    void testGetAllEmployees_ResponseNotEmpty() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.isEmptyString())));
    }

    @Test
    @DisplayName("GET all employees - should contain HATEOAS links")
    void testGetAllEmployees_HateoasLinks() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("API: Create employee valid")
    void testCreateEmployee_Valid() throws Exception {

        String json = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "john%s@gmail.com",
          "phoneNumber": "9876543210",
          "hireDate": "2024-01-01",
          "salary": 5000,
          "jobTitle": "Public Accountant",
          "departmentName": "Administration"
        }
        """.formatted(System.currentTimeMillis());

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST Employee - invalid data")
    void testCreateEmployee_Invalid() throws Exception {

        String json = """
    {
      "firstName": "",
      "email": "wrong",
      "salary": -100
    }
    """;

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(
                        result -> {
                            String content = result.getResponse().getContentAsString();

                            assert(content.contains("errors") || content.contains("message"));
                        }
                );
    }
}