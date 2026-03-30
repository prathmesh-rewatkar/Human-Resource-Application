package com.example.HumanResourceApplication.repository;

import com.example.HumanResourceApplication.entity.Job;
import com.example.HumanResourceApplication.entity.Department;
import com.example.HumanResourceApplication.entity.Employee;
import com.example.HumanResourceApplication.repository.EmployeeRepository;
import com.example.HumanResourceApplication.repository.JobRepository;
import com.example.HumanResourceApplication.repository.DepartmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired DepartmentRepository departmentRepository;

    @Test
    @DisplayName("findAll - validate employee fields")
    void testFindAll_ValidateEmployeeFields() {

        List<Employee> employees = employeeRepository.findAll();

        Employee employee = employees.get(0);

        assertThat(employee.getEmployeeId()).isNotNull();
        assertThat(employee.getFirstName()).isNotNull();
        assertThat(employee.getLastName()).isNotNull();
        assertThat(employee.getEmail()).isNotNull();
        assertThat(employee.getPhoneNumber()).isNotNull();
        assertThat(employee.getHireDate()).isNotNull();
        assertThat(employee.getSalary()).isNotNull();
    }

    @Test
    @DisplayName("findAll - should return consistent size")
    void testFindAll_SizeConsistency() {

        List<Employee> employees1 = employeeRepository.findAll();
        List<Employee> employees2 = employeeRepository.findAll();

        assertThat(employees1.size()).isEqualTo(employees2.size());
    }

    @Test
    @DisplayName("findAll - employee IDs should be unique")
    void testFindAll_UniqueIds() {

        List<Employee> employees = employeeRepository.findAll();

        long uniqueCount = employees.stream()
                .map(Employee::getEmployeeId)
                .distinct()
                .count();

        assertThat(uniqueCount).isEqualTo(employees.size());
    }

    @Test
    @DisplayName("findAll - employees should have valid data")
    void testFindAll_ValidData() {

        List<Employee> employees = employeeRepository.findAll();

        employees.forEach(emp -> {
            assertThat(emp.getEmployeeId()).isNotNull();
            assertThat(emp.getFirstName()).isNotBlank();
            assertThat(emp.getLastName()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Repository: Save valid employee")
    void testSaveEmployee_Valid() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Rakesh");
        emp.setLastName("Sharma");
        emp.setEmail("rakesh" + System.currentTimeMillis() + "@gmail.com");
        emp.setPhoneNumber("7894561238");
        emp.setHireDate(LocalDate.now());
        emp.setSalary(5000.0);
        emp.setJob(job);
        emp.setDepartment(dept);

        Employee saved = employeeRepository.save(emp);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmployeeId()).isNotNull();
    }

    @Test
    @DisplayName("Save employee - missing required fields")
    void testSaveEmployee_MissingFields() {

        Employee emp = new Employee();

        assertThatThrownBy(() -> {
            employeeRepository.save(emp);
            employeeRepository.flush();
        }).isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save employee - invalid email format")
    void testSaveEmployee_InvalidEmail() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("invalid-email");
        emp.setPhoneNumber("9876543210");
        emp.setHireDate(LocalDate.now());
        emp.setSalary(5000.0);
        emp.setJob(job);
        emp.setDepartment(dept);

        assertThatThrownBy(() -> {
            employeeRepository.save(emp);
            employeeRepository.flush();
        }).isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save employee - invalid phone number")
    void testSaveEmployee_InvalidPhone() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("test@gmail.com");
        emp.setPhoneNumber("123");
        emp.setHireDate(LocalDate.now());
        emp.setSalary(5000.0);
        emp.setJob(job);
        emp.setDepartment(dept);

        assertThatThrownBy(() -> {
            employeeRepository.save(emp);
            employeeRepository.flush();
        }).isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save employee - negative salary")
    void testSaveEmployee_InvalidSalary() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("test@gmail.com");
        emp.setPhoneNumber("9876543210");
        emp.setHireDate(LocalDate.now());
        emp.setSalary(-1000.0);
        emp.setJob(job);
        emp.setDepartment(dept);

        assertThatThrownBy(() -> {
            employeeRepository.save(emp);
            employeeRepository.flush();
        }).isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save employee - future hire date should fail")
    void testSaveEmployee_FutureHireDate() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("future@gmail.com");
        emp.setPhoneNumber("9876543210");
        emp.setHireDate(LocalDate.now().plusDays(5));
        emp.setSalary(5000.0);
        emp.setJob(job);
        emp.setDepartment(dept);

        assertThatThrownBy(() -> {
            employeeRepository.save(emp);
            employeeRepository.flush();
        }).isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save employee - invalid commission percentage")
    void testSaveEmployee_InvalidCommission() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setEmail("commission@gmail.com");
        emp.setPhoneNumber("9876543210");
        emp.setHireDate(LocalDate.now());
        emp.setSalary(5000.0);
        emp.setCommissionPct(1.5);
        emp.setJob(job);
        emp.setDepartment(dept);

        assertThatThrownBy(() -> {
            employeeRepository.save(emp);
            employeeRepository.flush();
        }).isInstanceOf(jakarta.validation.ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save employee - salary out of range should fail")
    void testSaveEmployee_SalaryOutOfRange() {

        Job job = jobRepository.findByJobTitleIgnoreCase("Public Accountant")
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Department dept = departmentRepository.findByDepartmentNameIgnoreCase("Administration")
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee emp = new Employee();
        emp.setFirstName("Rich");
        emp.setLastName("User");
        emp.setEmail("rich" + System.currentTimeMillis() + "@gmail.com");
        emp.setPhoneNumber("9876543210");
        emp.setHireDate(LocalDate.now());
        emp.setSalary(99999999.0);
        emp.setJob(job);
        emp.setDepartment(dept);

        assertThatThrownBy(() -> employeeRepository.saveAndFlush(emp))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("findByFirstNameAndLastName - valid data")
    void testFindByFirstNameAndLastName_Valid() {
        List<Employee> result = employeeRepository
                .findByFirstNameAndLastName("Jennifer", "Whalen");

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("findByFirstNameAndLastName - no result")
    void testFindByFirstNameAndLastName_NotFound() {
        List<Employee> result = employeeRepository
                .findByFirstNameAndLastName("Invalid", "User");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByFirstNameAndLastName - null input")
    void testFindByFirstNameAndLastName_Null() {
        List<Employee> result = employeeRepository
                .findByFirstNameAndLastName(null, null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByEmail - not found")
    void testFindByEmail_NotFound() {
        Optional<Employee> emp = employeeRepository.findByEmail("invalid@email.com");

        assertThat(emp).isEmpty();
    }

    @Test
    @DisplayName("findByJobTitle - valid")
    void testFindByJobTitle_Valid() {
        List<Employee> result = employeeRepository.findByJob_JobTitle("Stock Manager");

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("findByJobTitle - not found")
    void testFindByJobTitle_NotFound() {
        List<Employee> result = employeeRepository.findByJob_JobTitle("Unknown Job");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByDepartmentName - valid")
    void testFindByDepartmentName_Valid() {
        List<Employee> result = employeeRepository
                .findByDepartment_DepartmentName("IT");

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("findByDepartmentName - not found")
    void testFindByDepartmentName_NotFound() {
        List<Employee> result = employeeRepository
                .findByDepartment_DepartmentName("Unknown Dept");

        assertThat(result).isEmpty();
    }
}