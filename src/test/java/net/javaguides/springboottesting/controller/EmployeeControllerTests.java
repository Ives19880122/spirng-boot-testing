package net.javaguides.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest // 測試WebMvc
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 告訴Spring建立一個mock Service
     * 並且注入到controller內
     */
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Junit test for Create employee REST API
    @DisplayName("Junit test for Create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                /**
                 * 依據路徑對資料進行逐一比對
                 */
                .andExpect(jsonPath("$.firstname",
                        is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname",
                        is(employee.getLastname())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    // Junit test for Get All employees REST API
    @DisplayName("Junit test for Get All employees REST API")
    @Test
    public void givenListOfEmployee_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
            .id(1L)
            .firstname("Ives")
            .lastname("He")
            .email("ivesxxx@google.com.tw")
            .build());
        listOfEmployees.add(Employee.builder()
            .id(2L)
            .firstname("DDD")
            .lastname("XXX")
            .email("iDvaxxx@google.com.tw")
            .build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                /**
                 * 比對JSON資料
                 */
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));
    }

    // Junit test for GET employee by id REST API - positive scenario
    @DisplayName("Junit test for GET employee by id REST API - positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(1L)
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname",is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname",is(employee.getLastname())))
                .andExpect(jsonPath("$.email",is(employee.getEmail())));
    }

    // Junit test for GET employee by id REST API - negative scenario
    @DisplayName("Junit test for GET employee by id REST API - negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit test for update employee REST API - positive scenario
    @DisplayName("Junit test for update employee REST API - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstname("AAA")
                .lastname("DD")
                .email("sdsdccc@google.com.tw")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname",is(updatedEmployee.getFirstname())))
                .andExpect(jsonPath("$.lastname",is(updatedEmployee.getLastname())))
                .andExpect(jsonPath("$.email",is(updatedEmployee.getEmail())));
    }

    // Junit test for update employee REST API - negative scenario
    @DisplayName("Junit test for update employee REST API - negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstname("AAA")
                .lastname("DD")
                .email("sdsdccc@google.com.tw")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
