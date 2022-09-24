package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.exception.ResourceNotFoundException;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.*;

import net.javaguides.springboottesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // 如果要用Annotation注入Mock必須要加這段
public class EmployeeServiceTests {

    /**
     * 使用annotation注入的方式
     */
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
    }

    // Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);
        // when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method which throws exception")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
                employeeService.saveEmployee(employee);
        });

        // then - verify the output 前面已經拋出異常,所以接續的儲存方法不會執行
        verify(employeeRepository,never()).save(any(Employee.class));
    }
}
