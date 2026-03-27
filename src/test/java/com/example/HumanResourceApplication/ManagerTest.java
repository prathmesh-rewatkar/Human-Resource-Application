package com.example.HumanResourceApplication;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void testFindByEmployeeId() {

        Optional<Employee> result = employeeRepository.findByEmployeeId(100L);

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

        // ✅ Assertions
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("SHIGGINS");
    }


    @Test
    void testFindManagerByEmail_WrongEmail(){
        var result = employeeRepository
                .findDistinctBySubordinatesIsNotEmptyAndEmail("sam@gmail.com");

        // ✅ Assertions
        assertThat(result).isNull();
        //assertThat(result.getEmail()).isEqualTo("SHIGGINS");
    }


    @Test
    void testFindByManagerId_WithData(){
        List<EmployeeProjection> list=employeeRepository.findByManager_EmployeeId(101L);
        assertThat(list).isNotNull();
        assertThat(list.size()>0);
        list.forEach(m-> System.out.println(m.getFirstName()));
    }


    @Test
    void testFindByManagerId_NoData() {
        List<EmployeeProjection> list = employeeRepository.findByManager_EmployeeId(9999L);

        assertThat(list).isNotNull();
        assertThat(list.size()==0);
    }

    @Test
    void testFindHierarchy_ValidId(){
        List<Employee>list=employeeRepository.getHierarchy(101L);
        assertThat(list).isNotNull();
        assertThat(list.size()>0);
        list.forEach(m-> System.out.println(m.getEmployeeId()));
    }

    @Test
    void testFindHierarchy_InvalidId(){
        assertThrows(RuntimeException.class, () -> {
            employeeRepository.getHierarchy(9999L);
        });

    }

}





