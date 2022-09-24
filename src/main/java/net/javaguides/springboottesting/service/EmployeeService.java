package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
}
