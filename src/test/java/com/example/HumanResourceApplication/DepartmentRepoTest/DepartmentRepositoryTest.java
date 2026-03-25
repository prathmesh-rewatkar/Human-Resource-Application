package com.example.HumanResourceApplication.DepartmentRepoTest;

import com.example.HumanResourceApplication.entity.Department;
import com.example.HumanResourceApplication.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentRepositoryTest
{
    @Autowired
    private DepartmentRepository repository;

    private Department dept;

    @BeforeEach
    void setUp()
    {
        dept = new Department();
        dept.setName("Finance");
        repository.save(dept);
    }

    @Test
    void testFindAllDepartments() {
        var list = repository.findAll();

        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName()).isEqualTo("HR");
    }

//    @Test
//    void testFindById() {
//        var found = repository.findById(dept.getId());
//
//        assertThat(found).isPresent();
//        assertThat(found.get().getName()).isEqualTo("HR");
//    }

    @Test
    void testAddDepartment()
    {
        Department dept = new Department();
        dept.setName("HR");

        Department saved = repository.save(dept);

        Department fetched = repository.findById(saved.getId()).orElse(null);

        assertThat(fetched).isNotNull();

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("HR");

        System.out.println(saved);
    }
}
