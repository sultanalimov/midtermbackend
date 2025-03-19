package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeResourceIntegrationTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeResource employeeResource;

    private Employee mockEmployee;

    @BeforeEach
    void setUp() {
        // Initialize mock employee
        mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setName("John Doe");
        mockEmployee.setJobTitle("IT");
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(mockEmployee);
        when(employeeResource.getAllEmployees()).thenReturn((ResponseEntity<List<Employee>>) mockEmployeeList);

        ResponseEntity<List<Employee>> responseEntity = employeeResource.getAllEmployees();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockEmployeeList, responseEntity.getBody());
    }
}
