package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестирование в BDD (given, when, then) стиле с org.mockito.BDDMockito
 */
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	EmployeeRepository employeeRepository;

	@Test
	public void whenGETRoot_thenShowHomePageWithEmployees() throws Exception {
		// given
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com");
		emp1.setId(1L);
		Employee emp2 = new Employee("Jane", "Smith", "jane.smith@example.com");
		emp2.setId(2L);

		// Создаём список сотрудников
		// Example: Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName").ascending());
		Pageable pageable = PageRequest.of(0, 1);
		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), pageable, 2);

		// Мокаем репозиторий
		given(this.employeeRepository.findAll((Pageable) any())).willReturn(employeePage);

		// when + then
		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 2))
				.andExpect(model().attribute("employees", hasSize(2)))
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
		this.mockMvc.perform(get("/employees/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("create_employee"))
				.andExpect(model().attributeExists("employee"));
	}

	@Test
	public void whenPOSTCreateEmployee_thenRedirectToHome() throws Exception {
		// given
		Employee employee = new Employee("Jane", "Smith", "jane.smith@example.com");

		// when + then
		this.mockMvc.perform(post("/employees")
						.flashAttr("employee", employee))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/showEmployees"));

		then(this.employeeRepository).should().save(employee);
	}

	@Test
	public void whenGETEditFormWithValidId_thenShowEditPage() throws Exception {
		// given
		Employee employee = new Employee("Bob", "Brown", "bob.b@example.com");
		employee.setId(1L);
		given(this.employeeRepository.findById(1L)).willReturn(Optional.of(employee));

		// when + then
		this.mockMvc.perform(get("/employees/edit/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_employee"))
				.andExpect(model().attributeExists("employee"))
				.andExpect(model().attribute("employee", employee));
	}

	@Test
	public void whenGETEditFormWithInvalidId_thenThrowException() {
		given(this.employeeRepository.findById(999L)).willReturn(Optional.empty());
		String errorMessage = "";

		try {
			this.mockMvc.perform(get("/employees/edit/999"));
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
		this.mockMvc.perform(post("/employees/update/1")
						.flashAttr("employee", updatedEmployee))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		then(this.employeeRepository).should().save(updatedEmployee);
	}

	@Test
	public void whenGETDeleteEmployee_thenRedirectToHome() throws Exception {
		// given
		willDoNothing().given(this.employeeRepository).deleteById(1L);

		// when + then
		this.mockMvc.perform(get("/employees/delete/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/showEmployees"));

		then(this.employeeRepository).should().deleteById(1L);
	}

	@Test
	public void shouldReturnFilteredEmployeesPage() throws Exception {
		// Given
		Employee emp1 = new Employee();
		emp1.setId(1L);
		emp1.setFirstName("Анна");
		emp1.setLastName("Иванова");
		emp1.setEmail("anna@example.com");

		Page<Employee> employeePage = new PageImpl<>(List.of(emp1), PageRequest.of(0, 10), 1);

		given(this.employeeRepository.findByFiltersAndSort(
				eq("Анна"), eq("Иванова"), eq("anna@example.com"), any(Pageable.class)))
				.willReturn(employeePage);

		// When & Then
		this.mockMvc.perform(get("/showEmployees")
						.param("firstName", "Анна")
						.param("lastName", "Иванова")
						.param("email", "anna@example.com")
						.param("page", "0")
						.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("showEmployees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 1))
				.andExpect(model().attribute("totalElements", 1L))
				.andExpect(model().attribute("firstName", "Анна"))
				.andExpect(model().attribute("lastName", "Иванова"))
				.andExpect(model().attribute("email", "anna@example.com"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Анна")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Иванова")));
	}

	@Test
	public void shouldReturnAllEmployeesWhenNoFilters() throws Exception {
		// Мой тест
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

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), pageable, 2);

		given(this.employeeRepository.findByFiltersAndSort(isNull(), isNull(), isNull(), any(Pageable.class)))
				.willReturn(employeePage);

		this.mockMvc.perform(get("/showEmployees")
						.param("page", "0")
						.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("showEmployees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 1))
				.andExpect(model().attribute("totalElements", 2L))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Анна")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Петр")));
	}

	@Test
	public void shouldReturnAllEmployeesWhenNoFilters_Giga() throws Exception {
		// Test from GidaCode
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

		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), pageable, 2);

		// Исправлено: метод findByFiltersAndSort с учётом sort
		given(this.employeeRepository.findByFiltersAndSort(
				isNull(), isNull(), isNull(), any(Pageable.class)))
				.willReturn(employeePage);

		// When & Then
		this.mockMvc.perform(get("/showEmployees")
						.param("page", "0")
						.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("showEmployees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 1))
				.andExpect(model().attribute("totalElements", 2L))
				.andExpect(model().attribute("sortField", "id"))
				.andExpect(model().attribute("sortDirection", "asc"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Анна")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Петр")));
	}

}