package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.*;

import net.javaguides.springboottesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

public class EmployeeServiceTests {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    /**
     * 使用一般建構子注入的方式
     */
    @BeforeEach
    public void setup(){
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    // Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


}
