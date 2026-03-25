package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private PositionRepository positionRepository;

	@Test
	public void shouldListEmployeesWithPagination() throws Exception {
		// Given
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "empl1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "empl2@example.com", position);
		emp2.setId(2L);

		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), PageRequest.of(0, 10), 2);

		given(employeeRepository.findAll(any(Pageable.class))).willReturn(employeePage);

		// When & Then
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 1))
				.andExpect(model().attribute("totalElements", 2L))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Firstname1")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Firstname2")));
	}

	@Test
    public void shouldShowHomePageWithEmployees() throws Exception {
		Position position = new Position(1L, "Developer");
        // Given
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
//		Employee emp3 = new Employee("Firstname3", "Lastname3", "emp3@example.com", position);
//		emp3.setId(3L);

		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), PageRequest.of(0, 2), 2);
		PageRequest pageRequest = PageRequest.of(0, 2);
		Pageable pageable = PageRequest.of(0, 2);
		given(employeeRepository.findAll(any(pageable.getClass()))).willReturn(employeePage);

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("totalElements", 2L));
    }

    @Test
    public void shouldShowCreateForm() throws Exception {
        // Given
        given(positionRepository.findAll()).willReturn(java.util.Arrays.asList(new Position(1L, "Developer")));

        // When & Then
        mockMvc.perform(get("/employees/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_employee"))
                .andExpect(model().attributeExists("employee", "positions"));
    }

    @Test
    public void shouldCreateEmployeeAndRedirect() throws Exception {
        // Given
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", new Position(1L, "Manager"));

        // When & Then
        mockMvc.perform(post("/employees")
                        .flashAttr("employee", employee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/show_employees"));

        verify(employeeRepository).save(employee);
    }

    @Test
    public void shouldShowEditFormForExistingEmployee() throws Exception {
        // Given
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
        employee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(positionRepository.findAll()).willReturn(java.util.Arrays.asList(position));

        // When & Then
        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_employee"))
                .andExpect(model().attributeExists("employee", "positions"))
                .andExpect(model().attribute("employee", employee));
    }

    @Test
    public void shouldReturn404WhenEditEmployeeNotFound() throws Exception {
        // Given
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/employees/edit/999"))
                .andExpect(result -> org.assertj.core.api.Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Employee not exist with id=999"));
    }

    @Test
    public void shouldUpdateEmployeeAndRedirect() throws Exception {
        // Given
        Employee updatedEmployee = new Employee("Firstname1", "Lastname1", "empl1@example.com", new Position(1L, "Manager"));
        updatedEmployee.setId(1L);

        // When & Then
        mockMvc.perform(post("/employees/update/1")
                        .flashAttr("employee", updatedEmployee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(employeeRepository).save(updatedEmployee);
    }

    @Test
    public void shouldDeleteEmployeeAndRedirect() throws Exception {
        // When & Then
        mockMvc.perform(get("/employees/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    public void shouldShowFilteredEmployees() throws Exception {
        // Given
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", new Position(1L, "Developer"));
        Page<Employee> page = new PageImpl<>(java.util.Arrays.asList(employee));

        given(employeeRepository.findByFiltersAndSort(
                eq("John"), isNull(), isNull(), isNull(), any(Pageable.class)))
                .willReturn(page);

        // When & Then
        mockMvc.perform(get("/show_employees")
                        .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(view().name("show_employees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("John")));
    }

    @Test
    public void shouldShowAllEmployeesWithoutFilters() throws Exception {
        // Given
		Position position = new Position(1L, "Developer");
		// Given
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
        Page<Employee> page = new PageImpl<>(java.util.Arrays.asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 1, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				"f", "l", "e", 0L, pageable)).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/show_employees?page=0&size=1&firstName=f&lastName=l&email=e&positionId=0&sortField=id&direction=asc" ))
                .andExpect(status().isOk())
                .andExpect(view().name("show_employees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("totalElements", 2L));
    }
}