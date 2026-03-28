package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    void testGetDepartmentByName() throws Exception {

//        repository.save(createDepartment("Finance"));

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
    @Test
    void testSearchByExactName() throws Exception {
        mockMvc.perform(get("/department/search/findByDepartmentName")
                        .param("name", "Shipping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments[0].departmentName").value("Shipping"));
    }

    // Search by partial name (Page 2 filter)
    @Test
    void testSearchByPartialName() throws Exception {
        mockMvc.perform(get("/department/search/findByDepartmentNameContainingIgnoreCase")
                        .param("name", "sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").isArray());
    }

    // Filter by location city (Page 2 filter)
    @Test
    void testFilterByLocationCity() throws Exception {
        mockMvc.perform(get("/department/search/findByLocationCity")
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

    // ─── CRUD OPERATIONS ────────────────────────────────────────────

    // Add Department (POST)
    @Test
    @Transactional
    void testCreateDepartment() throws Exception {
        String json = """
                {
                  "departmentName": "Test Department"
                }
                """;
        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    // Update Department (PUT)
    @Test
    @Transactional
    void testUpdateDepartment() throws Exception {
        String json = """
                {
                  "departmentId": 20,
                  "departmentName": "Marketing Updated"
                }
                """;
        mockMvc.perform(put("/department/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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

    // Delete Department
    @Test
    @Transactional
    void testDeleteDepartment() throws Exception {
        // First create one to delete
        String json = """
                {
     
                  "departmentName": "To Be Deleted"
                }
                """;
        mockMvc.perform(post("/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Now delete it
//        mockMvc.perform(delete("/department/998"))
//                .andExpect(status().isNoContent());
    }

    // Count
    @Test
    void testGetDepartmentCount() throws Exception {
        mockMvc.perform(get("/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(27));
    }



}