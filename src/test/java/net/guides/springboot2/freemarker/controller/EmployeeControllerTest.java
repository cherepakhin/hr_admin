package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void whenGETRoot_thenShowHomePageWithEmployees() throws Exception {
        // given
        Employee emp1 = new Employee("John", "Doe", "john.doe@example.com");
        emp1.setId(1L);
        given(employeeRepository.findAll()).willReturn(List.of(emp1));

        // when + then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", hasSize(1)))
				.andExpect(model().attribute("employees", hasItem(
						allOf(
								hasProperty("firstName", is("John")),
								hasProperty("lastName", is("Doe")),
								hasProperty("email", is("john.doe@example.com"))
						)
				)));
    }

    @Test
    public void whenGETNewForm_thenShowCreateForm() throws Exception {
        // when + then
        mockMvc.perform(get("/employees/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_employee"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    public void whenPOSTCreateEmployee_thenRedirectToHome() throws Exception {
        // given
        Employee employee = new Employee("Jane", "Smith", "jane.smith@example.com");

        // when + then
        mockMvc.perform(post("/employees")
                        .flashAttr("employee", employee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        then(employeeRepository).should().save(employee);
    }

    @Test
    public void whenGETEditFormWithValidId_thenShowEditPage() throws Exception {
        // given
        Employee employee = new Employee("Bob", "Brown", "bob.b@example.com");
        employee.setId(1L);
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when + then
        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_employee"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employee", employee));
    }

    @Test
    public void whenGETEditFormWithInvalidId_thenThrowException() throws Exception {
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());
		String errorMessage = "";

		try {
			mockMvc.perform(get("/employees/edit/999"));
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}

		Assertions.assertThat(errorMessage).isEqualTo("Request processing failed: java.lang.IllegalArgumentException: Employee not exist with id=999");
    }

    @Test
    public void whenPOSTUpdateEmployee_thenRedirectToHome() throws Exception {
        // given
        Employee updatedEmployee = new Employee("Robert", "Brown", "robert.b@example.com");
        updatedEmployee.setId(1L);

        // when + then
        mockMvc.perform(post("/employees/update/1")
                        .flashAttr("employee", updatedEmployee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        then(employeeRepository).should().save(updatedEmployee);
    }

    @Test
    public void whenGETDeleteEmployee_thenRedirectToHome() throws Exception {
        // given
        willDoNothing().given(employeeRepository).deleteById(1L);

        // when + then
        mockMvc.perform(get("/employees/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        then(employeeRepository).should().deleteById(1L);
    }

    @Test
    public void whenGETShowEmployees_thenShowAllEmployeesPage() throws Exception {
        // given
        Employee emp1 = new Employee("John", "Doe", "john.doe@example.com");
        emp1.setId(1L);
        given(employeeRepository.findAll()).willReturn(Arrays.asList(emp1));

        // when + then
        mockMvc.perform(get("/showEmployees"))
                .andExpect(status().isOk())
                .andExpect(view().name("showEmployees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", hasSize(1)));
    }
}