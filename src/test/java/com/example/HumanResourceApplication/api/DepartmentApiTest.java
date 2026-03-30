package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class DepartmentApiTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository repository;

//    --------------- PAGE 2 - Department List----------------

    // Get All Departments
    @Test
    void testGetAllDepartments() throws Exception
    {
        mockMvc.perform(get("/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").isArray())
                .andExpect(jsonPath("$._embedded.departments[0].departmentId").doesNotExist())
                .andExpect(jsonPath("$._embedded.departments[0].departmentName").exists());
    }

    // Get Department By ID with projection (Valid)
    @Test
    void testGetDepartmentById() throws Exception {
        mockMvc.perform(get("/department/20")
                        .param("projection", "deptView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("Marketing"))
                .andExpect(jsonPath("$.departmentId").doesNotExist())
                .andExpect(jsonPath("$._embedded.location.city").exists())
                .andExpect(jsonPath("$._embedded.location.streetAddress").exists());
    }

    // Get Department By ID - Not Found(Invalid)
    @Test
    void testGetDepartmentById_NotFound() throws Exception
    {
        mockMvc.perform(get("/department/9999"))
                .andExpect(status().isNotFound());
    }

    // Search Department By Name
    @Test
    void testGetDepartmentByName() throws Exception
    {
        mockMvc.perform(get("/department/search/findByDepartmentName")
                        .param("name", "Shipping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments[0].departmentName").value("Shipping"));
    }

    // Search Department By Name (No Result)
    @Test
    void testGetDepartmentByName_NotFound() throws Exception {

        mockMvc.perform(get("/department/search/findByDepartmentName")
                        .param("name", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.department").doesNotExist());
    }

    // Search by exact name (Page 2 filter)
//    @Test
//    void testSearchByExactName() throws Exception {
//        mockMvc.perform(get("/department/search/findByDepartmentName")
//                        .param("name", "Shipping"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$._embedded.departments[0].departmentName").value("Shipping"));
//    }

    // Search by partial name (Page 2 filter)
//    @Test
//    void testSearchByPartialName() throws Exception {
//        mockMvc.perform(get("/department/search/findByDepartmentNameContainingIgnoreCase")
//                        .param("name", "sales"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$._embedded.departments").isArray());
//    }

    // Filter by location city (Page 2 filter)
    @Test
    void testFilterByLocationCity() throws Exception {
        mockMvc.perform(get("/department/search/findByLocation_City")
                        .param("city", "Seattle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").isArray());
    }

    // Filter departments with manager assigned
    @Test
    void testFilterDepartmentsWithManager() throws Exception {
        mockMvc.perform(get("/department/search/findByManagerIsNotNull"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").isArray());
    }

    // ─── PAGE 3: EMPLOYEES IN A DEPARTMENT ──────────────────────────

    // Get employees of a department via association link
    @Test
    void testGetEmployeesInDepartment() throws Exception {
        mockMvc.perform(get("/department/50/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employees").isArray());
    }

    // Get manager of a department
    @Test
    void testGetDepartmentManager() throws Exception {
        mockMvc.perform(get("/department/20/manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());
    }

    // Get location of a department
    @Test
    void testGetDepartmentLocation() throws Exception {
        mockMvc.perform(get("/department/20/location"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").exists());
    }

    // ──────────────────────────── CRUD OPERATIONS ─────────────────────────────────

    // Add Department (POST)
    @Test
    @Transactional
    void testCreateDepartment() throws Exception {
        String json = """
                {
                  "departmentName": "Test Department",
                    "location": "http://localhost/locations/1700",
                    "manager": "http://localhost/employees/100"
                }
                """;
        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    // Add Duplicate Department with same location (POST)
    @Test
    void testCreateDuplicateDepartment() throws Exception {
        // First create
        String json = """
            {
              "departmentName": "TestDup123",
              "location": "http://localhost/locations/1700"
            }
            """;
        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Try creating exact duplicate — should fail with 409
        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); // 409
    }


    // Update Department (PUT)
    @Test
    @Transactional
    void testUpdateDepartment() throws Exception {
        String json = """
                {
                  "departmentName": "Marketing Updated"
                }
                """;
        mockMvc.perform(put("/department/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNoContent()); //204
    }

    // Partial Update (PATCH)
    @Test
    @Transactional
    void testPatchDepartment() throws Exception {
        String json = """
                {
                  "departmentName": "Marketing Patched"
                }
                """;
        mockMvc.perform(patch("/department/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent()); //204
    }

    // Partial Update — only location (PATCH)
    @Test
    void testPatchDepartmentLocation() throws Exception {
        String json = """
            {
              "location": "http://localhost/locations/1700"
            }
            """;
        mockMvc.perform(patch("/department/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }

    // Partial Update — only manager (PATCH)
    @Test
    void testPatchDepartmentManager() throws Exception {
        String json = """
            {
              "manager": "http://localhost/employees/201"
            }
            """;
        mockMvc.perform(patch("/department/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }

    // Delete Department
    @Test
    @Transactional
    void testDeleteDepartment() throws Exception {
        // First create one to delete
        String json = """
                {
     
                  "departmentName": "DeleteTest123",
                  "location": "http://localhost/locations/1700"
                }
                """;
        String location = mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");  // auto-generated ID from Location header

        assertThat(location).isNotNull();

        // Now delete it using returned location url
        mockMvc.perform(delete(new java.net.URI(location)))
                .andExpect(status().isNoContent());
    }

    // When you pass location directly as a String, Java gets confused because the String version
    // expects a URL template pattern like /department/{id}, not a full URL like http://localhost/department/28.
    // Wrapping it in new java.net.URI(location) uses the correct overload and resolves the issue.

    // Count
    @Test
    void testGetDepartmentCount() throws Exception {
        mockMvc.perform(get("/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(27));
    }



}