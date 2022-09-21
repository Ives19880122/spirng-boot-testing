package net.javaguides.springboottesting.repository;

import net.javaguides.springboottesting.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest    // 只測試Repository, 不會載入Service,Controller
public class EmployeeRepositoryTests {
    @Autowired EmployeeRepository employeeRepository;

    // Junit test for save employee operation
    @DisplayName("Junit test for save employee operation")  // 設定測試的顯示名稱
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);
        // then verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }
    // Junit test for get all employees operation
    @DisplayName("Junit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        Employee employee1 = Employee.builder()
                .firstname("John")
                .lastname("Dada")
                .email("dadaxxx@google.com.tw")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // Junit test for get employee by id operation
    @DisplayName("Junit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    // Junit test for get employee by email operation
    @DisplayName("Junit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }
}
