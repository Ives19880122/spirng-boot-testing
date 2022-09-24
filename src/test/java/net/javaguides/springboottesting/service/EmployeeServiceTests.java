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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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

    // Junit test for getAllEmployees method
    @DisplayName("Junit test for getAllEmployees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeList(){
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstname("DDD")
                .lastname("XXX")
                .email("iDvaxxx@google.com.tw")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // Junit test for getAllEmployees method
    @DisplayName("Junit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployee_thenReturnEmptyEmployeeList(){
        // given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behavior that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // Junit test for method getEmployeeById
    @DisplayName("Junit test for method getEmployeeById")
    @Test
    public void givenEmployId_whenGetEmployeeById_thenReturnEmployeeObject(){
        // given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        Employee queryEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then - verify the output
        assertThat(queryEmployee).isNotNull();

    }

    // Junit test for updateEmployee method
    @DisplayName("Junit test for updateEmployee method")
    @Test
    public void givenEmployId_whenUpdateEmployee_thenReturnEmptyObject(){
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("xxsddfs@gmail.com");
        employee.setFirstname("ccc");

        // when - action or the behavior that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("xxsddfs@gmail.com");
        assertThat(updatedEmployee.getFirstname()).isEqualTo("ccc");
    }

    // Junit test for deleteEmployee method
    @DisplayName("Junit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given - precondition or setup
        long employeeId = 1L;

        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behavior that we are going test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }
}
