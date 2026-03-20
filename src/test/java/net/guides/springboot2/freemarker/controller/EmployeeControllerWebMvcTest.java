package net.guides.springboot2.freemarker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnFilteredEmployeesPage() throws Exception {
        // Given
        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setFirstName("Анна");
        emp1.setLastName("Иванова");
        emp1.setEmail("anna@example.com");

        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1), PageRequest.of(0, 10), 1);

        given(employeeRepository.findByFilters(
                "Анна", null, null, PageRequest.of(0, 10)))
                .willReturn(employeePage);

        // When & Then
        mockMvc.perform(get("/showEmployees")
                        .param("firstName", "Анна")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("showEmployees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalElements", 1L))
                .andExpect(model().attribute("firstName", "Анна"))
                .andExpect(model().attribute("lastName", (Object) null))
                .andExpect(model().attribute("email", (Object) null))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Анна")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Иванова")));
    }

    @Test
    public void shouldReturnAllEmployeesWhenNoFilters() throws Exception {
        // Given
        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setFirstName("Анна");
        emp1.setLastName("Иванова");
        emp1.setEmail("anna@example.com");

        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setFirstName("Петр");
        emp2.setLastName("Сидоров");
        emp2.setEmail("petr@example.com");

        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), PageRequest.of(0, 10), 2);

        given(employeeRepository.findByFilters(null, null, null, PageRequest.of(0, 10)))
                .willReturn(employeePage);

        // When & Then
        mockMvc.perform(get("/showEmployees")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("showEmployees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalElements",2L))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Анна")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Петр")));
    }
}