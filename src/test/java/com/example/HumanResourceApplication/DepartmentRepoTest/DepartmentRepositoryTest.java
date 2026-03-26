//package com.example.HumanResourceApplication.DepartmentRepoTest;
//
//import com.example.HumanResourceApplication.entity.Department;
//import com.example.HumanResourceApplication.repository.DepartmentRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class DepartmentRepositoryTest
//{
//    @Autowired
//    private DepartmentRepository repository;
//
//    private Department dept;
//
//    @BeforeEach
//    void setUp()
//    {
//        dept = new Department();
//        dept.setName("Finance");
//        repository.save(dept);
//    }
//
//    // Get All Departments
//    @Test
//    void testFindAllDepartments() {
//        var list = repository.findAll();
//
//        assertThat(list).isNotEmpty();
//        assertThat(list.get(0).getName()).isEqualTo("HR");
//    }
//
//    // Get Department By ID
//    @Test
//    void testFindDepartmentById() {
//        Department dept = repository.save(new Department(null, "HR"));
//
//        Department found = repository.findById(dept.getId()).orElse(null);
//
//        assertThat(found).isNotNull();
//        assertThat(found.getName()).isEqualTo("HR");
//    }
//
//    // Add Department
//    @Test
//    void testAddDepartment()
//    {
//        Department dept = new Department();
//        dept.setName("HR");
//
//        Department saved = repository.save(dept);
//
//        Department fetched = repository.findById(saved.getId()).orElse(null);
//
//        assertThat(fetched).isNotNull();
//
//        assertThat(saved).isNotNull();
//        assertThat(saved.getId()).isNotNull();
//        assertThat(saved.getName()).isEqualTo("HR");
//
//        System.out.println(saved);
//    }
//
//    // Search Department By Name
//    @Test
//    void testFindByName()
//    {
//        repository.save(new Department(null, "HR"));
//        List<Department> result = repository.findByName("HR");
//        assertThat(result).isNotEmpty();
//    }
//
//    // Count Departments
//    @Test
//    void testCountDepartments() {
//        repository.save(new Department(null, "HR"));
//        repository.save(new Department(null, "Accounts"));
//
//        long count = repository.count();
//
//        assertThat(count).isEqualTo(6);
//    }
//
//    // Exists By ID
//    @Test
//    void testExistsById() {
//        Department dept = repository.save(new Department(null, "HR"));
//
//        boolean exists = repository.existsById(dept.getId());
//
//        assertThat(exists).isTrue();
//    }
//}
