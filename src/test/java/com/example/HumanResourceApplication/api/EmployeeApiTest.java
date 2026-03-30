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
                .andExpect(jsonPath("$._embedded.employees.length()")
                        .value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    @DisplayName("GET all employees - response should not be empty")
    void testGetAllEmployees_ResponseNotEmpty() throws Exception {

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.isEmptyString())));
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

    @Test
    @DisplayName("API: Create employee - invalid email")
    void testCreateEmployee_InvalidEmail() throws Exception {

        String json = """
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "wrong-email",
      "phoneNumber": "9876543210",
      "hireDate": "2024-01-01",
      "salary": 5000
    }
    """;

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("API: Create employee - missing required fields")
    void testCreateEmployee_MissingFields() throws Exception {

        String json = """
    {
      "firstName": "",
      "salary": 5000
    }
    """;

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("API: Create employee - salary out of range")
    void testCreateEmployee_SalaryOutOfRange() throws Exception {

        String json = """
    {
      "firstName": "Rich",
      "lastName": "User",
      "email": "rich%s@gmail.com",
      "phoneNumber": "9876543210",
      "hireDate": "2024-01-01",
      "salary": 99999999,
      "jobTitle": "Public Accountant",
      "departmentName": "Administration"
    }
    """.formatted(System.currentTimeMillis());

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("API: Create employee - invalid phone")
    void testCreateEmployee_InvalidPhone() throws Exception {

        String json = """
    {
      "firstName": "Test",
      "lastName": "User",
      "email": "test%s@gmail.com",
      "phoneNumber": "123",
      "hireDate": "2024-01-01",
      "salary": 5000
    }
    """.formatted(System.currentTimeMillis());

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("API: Get employee - not found")
    void testGetEmployee_NotFound() throws Exception {

        mockMvc.perform(get("/employees/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("API: Search by first and last name - valid")
    void testSearchByFullName_Valid() throws Exception {

        mockMvc.perform(get("/employees/search/findByFirstNameAndLastName")
                        .param("firstName", "Hermann")
                        .param("lastName", "Brown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees").isArray());
    }

    @Test
    @DisplayName("API: Search by job title - valid")
    void testSearchByJobTitle_Valid() throws Exception {

        mockMvc.perform(get("/employees/search/findByJob_JobTitle")
                        .param("jobTitle", "Stock Manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees").exists());
    }

    @Test
    @DisplayName("API: Search by department name - valid")
    void testSearchByDepartment_Valid() throws Exception {

        mockMvc.perform(get("/employees/search/findByDepartment_DepartmentName")
                        .param("departmentName", "Marketing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees").exists());
    }
}