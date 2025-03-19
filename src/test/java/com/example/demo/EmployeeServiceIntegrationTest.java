package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Employee;
import com.example.demo.repo.EmployeeRepo;
import com.example.demo.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceIntegrationTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee mockEmployee;

    @BeforeEach
    void setUp() {
        mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setName("Nureles");
        mockEmployee.setJobTitle("web-developer");
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(mockEmployee));

        Employee foundEmployee = employeeService.findEmployeeById(1L);

        assertEquals(mockEmployee, foundEmployee);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(mockEmployee);
        when(employeeRepo.findAll()).thenReturn(mockEmployeeList);

        List<Employee> foundEmployees = employeeService.findAllEmployees();

        assertEquals(mockEmployeeList, foundEmployees);
    }
}
