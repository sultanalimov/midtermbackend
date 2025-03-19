package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmployeeResourceTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeResource employeeResource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeResource).build();
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee("Nureles","nureles5@gmail.com","web-developer","996000000000","https"),
                new Employee("Kurmanbek","kurmanbek5@gmail.com","doctor","996000000000","https"));

        when(employeeService.findAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Nureles"))
                .andExpect(jsonPath("$[1].name").value("Kurmanbek"));

        verify(employeeService, times(1)).findAllEmployees();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Long id = 1L;
        Employee employee = new Employee("Nureles","nureles5@gmail.com","web-developer","996000000000","https");

        when(employeeService.findEmployeeById(id)).thenReturn(employee);

        mockMvc.perform(get("/employee/find/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Nureles"));

        verify(employeeService, times(1)).findEmployeeById(id);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee("Nureles","nureles5@gmail.com","web-developer","996000000000","https");

        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employee/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Nureles\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nureles"));

        verify(employeeService, times(1)).addEmployee(any(Employee.class));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee("Nureles","nureles5@gmail.com","web-developer","996000000000","https");

        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/employee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Nureles\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nureles"));

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/employee/delete/{id}", id))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(id);
        verifyNoMoreInteractions(employeeService);
    }
}
