package com.example.HumanResourceApplication.repository;


import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.projection.EmployeeProjection;
import com.example.HumanResourceApplication.projection.ManagerProjection;
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

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManagerRepositoryTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testFindByEmployeeId_ValidId() {

        Optional<Employee> result = employeeRepository.findByEmployeeId( 100);

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
        List<EmployeeProjection> list=employeeRepository.findByManager_EmployeeId(101);
        assertThat(list).isNotNull();
        assertThat(list.size()>0);
        list.forEach(m-> System.out.println(m.getFirstName()));
    }


    @Test
    void testFindByManagerId_NoData() {
        List<EmployeeProjection> list = employeeRepository.findByManager_EmployeeId(9999);

        assertThat(list).isNotNull();
        assertThat(list.size()==0);
    }

    @Test
    void testFindHierarchy_ValidId(){
        List<Employee>list=employeeRepository.getHierarchy(101);
        assertThat(list).isNotNull();
        assertThat(list.size()>0);
        list.forEach(m-> System.out.println(m.getEmployeeId()));
    }

    @Test
    void testFindHierarchy_InvalidId(){
        assertThrows(RuntimeException.class, () -> {
            employeeRepository.getHierarchy(9999);
        });

    }

}
