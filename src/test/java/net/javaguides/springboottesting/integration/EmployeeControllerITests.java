package net.javaguides.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 整合測試
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        // 執行每段測試前,清空資料
        employeeRepository.deleteAll();
    }

    // Junit test for Create employee REST API
    @DisplayName("Junit test for Create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
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
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build());
        listOfEmployees.add(Employee.builder()
                .firstname("DDD")
                .lastname("XXX")
                .email("iDvaxxx@google.com.tw")
                .build());
        employeeRepository.saveAll(listOfEmployees);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));
    }

    // Junit test for GET employee by id REST API - positive scenario
    @DisplayName("Junit test for GET employee by id REST API - positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employee.getId()));

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
        long employeeId = 0L;
        Employee employee = Employee.builder()
                .firstname("Ives")
                .lastname("He")
                .email("ivesxxx@google.com.tw")
                .build();
        employeeRepository.save(employee);

        // when - action or the behavior that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
