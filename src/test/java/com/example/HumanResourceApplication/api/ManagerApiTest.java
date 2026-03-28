package com.example.HumanResourceApplication.api;

import com.example.HumanResourceApplication.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ManagerApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void test_getAllManagers() throws Exception {
        mockMvc.perform(get("/api/v1/managers"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetManagerByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/manager/by-email")
                        .param("email", "SHIGGINS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("SHIGGINS"));
    }

    @Test
    void testgetAllManagersByDepartment() throws Exception {
        mockMvc.perform(get("/api/v1/manager/by-department")
                        .param("departmentName", "Shipping"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateManager_Success() throws Exception {

        String requestBody = """
        {
          "employeeId":108,
          "newManagerId":101
        }
        """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Manager updated successfully"));
    }

    @Test
    void testUpdateManager_EmployeeNotFound() throws Exception {

        String requestBody = """
        {
          "employeeId":1000,
          "newManagerId":101
        }
        """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    void testUpdateManager_ManagerNotFound() throws Exception {

        String requestBody = """
        {
          "employeeId":101,
          "newManagerId":9999
        }
        """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("New Manager not found"));
    }

    @Test
    void testUpdateManager_EmployeeCantBeSelfManager() throws Exception {

        String requestBody = """
        {
          "employeeId":101,
          "newManagerId":101
        }
        """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee cannot be their own manager"));
    }

    @Test
    void testUpdateManager_EmployeeCycle() throws Exception {

        String requestBody = """
        {
          "employeeId":100,
          "newManagerId":101
        }
        """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Circular hierarchy detected"));
    }
}