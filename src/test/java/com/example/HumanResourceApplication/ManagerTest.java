package com.example.HumanResourceApplication;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManagerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testGetManagerByEmail() throws Exception {

        mockMvc.perform(get("/api/v1/manager/by-email")
                        .param("email", "SHIGGINS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("SHIGGINS"));
    }

    @Test
    void testgetAllManagers()throws Exception{
        mockMvc.perform(get("/api/v1/managers"))
                .andExpect(status().isOk());
    }


    @Test
    void testgetAllManagersByDepartment()throws Exception{
        mockMvc.perform(get("/api/v1/manager/by-department")
                .param("departmentName","Shipping"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByEmployeeId_ValidId() {

        Optional<Employee> result = employeeRepository.findByEmployeeId((double) 100);

        assertTrue(result.isPresent());
        System.out.println(result.get().getFirstName());
    }


    @Test
    void testGetManagers() {


        List<ManagerProjection> managers = employeeRepository.findDistinctBySubordinatesIsNotEmpty();

        assertNotNull(managers);
        assertTrue(managers.size() > 0);
        managers.forEach(m -> System.out.println(m.getFirstName()));
    }

    @Test
    void testFindManagerByEmail_CorrectEmail(){
        var result = employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndEmail("SHIGGINS");

        //  Assertions
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("SHIGGINS");
    }

    @Test
    void testFindManagerByEmail_WrongEmail(){
        var result = employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndEmail("sam@gmail.com");

        //  Assertions
        assertThat(result).isNull();
        //assertThat(result.getEmail()).isEqualTo("SHIGGINS");
    }

    @Test
    void testFindManagerByDepartmentName_ValidData(){
        List<ManagerProjection>list=employeeRepository.findDistinctBySubordinatesIsNotEmptyAndDepartment_DepartmentName("Shipping");
        assertThat(list).isNotNull();
        System.out.println(list.get(0).getEmail());
        //assertThat(list.get(0).getEmail())
    }

    @Test
    void testFindManagerByDepartmentName_InvalidData(){
        List<ManagerProjection>list=employeeRepository.findDistinctBySubordinatesIsNotEmptyAndDepartment_DepartmentName("Unknown");
        assertThat(list.isEmpty());
        //System.out.println(list.get(0).getEmail());
        //assertThat(list.get(0).getEmail())
    }




    @Test
    void testFindByManagerId_WithData(){
        List<EmployeeProjection> list=employeeRepository.findByManager_EmployeeId((double)101);
        assertThat(list).isNotNull();
        assertThat(list.size()>0);
        list.forEach(m-> System.out.println(m.getFirstName()));
    }


    @Test
    void testFindByManagerId_NoData() {
        List<EmployeeProjection> list = employeeRepository.findByManager_EmployeeId((double)9999);

        assertThat(list).isNotNull();
        assertThat(list.size()==0);
    }

    @Test
    void testFindHierarchy_ValidId(){
        List<Employee>list=employeeRepository.getHierarchy((double)101);
        assertThat(list).isNotNull();
        assertThat(list.size()>0);
        list.forEach(m-> System.out.println(m.getEmployeeId()));
    }

    @Test
    void testFindHierarchy_InvalidId(){
        assertThrows(RuntimeException.class, () -> {
            employeeRepository.getHierarchy((double)9999);
        });

    }


    @Test
    @Transactional
    void testUpdateManager_Success() throws Exception {

        String requestBody = """
        {
          "employeeId": 108,
          "newManagerId": 101
        }
        """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())   // 👈 ADD THIS
                .andExpect(status().isOk());

    }


    @Test
    @Transactional
    void testUpdateManager_EmployeeNotFound() throws Exception {

        String requestBody = """
    {
      "employeeId": 9999,
      "newManagerId": 101
    }
    """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())   // or 404 if using custom exception
                .andExpect(jsonPath("$.error").value("Employee not found"));
    }


    @Test
    @Transactional
    void testUpdateManager_ManagerNotFound() throws Exception {

        String requestBody = """
    {
      "employeeId":101,
      "newManagerId": 9999
    }
    """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())   // or 404 if using custom exception
                .andExpect(jsonPath("$.error").value("New Manager not found"));
    }


    @Test
    @Transactional
    void testUpdateManager_EmployeeCantBeSelfManager() throws Exception {

        String requestBody = """
    {
      "employeeId":101,
      "newManagerId": 101
    }
    """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())   // or 404 if using custom exception
                .andExpect(jsonPath("$.error").value("Employee cannot be their own manager"));
    }


    @Test
    @Transactional
    void testUpdateManager_EmployeeCycle() throws Exception {

        String requestBody = """
    {
      "employeeId":100,
      "newManagerId": 101
    }
    """;

        mockMvc.perform(put("/api/v1/update-manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())   // or 404 if using custom exception
                .andExpect(jsonPath("$.error").value("Circular hierarchy detected"));
    }


    }

