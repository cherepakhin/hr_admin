package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
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
	private MockMvc mockMvc;

	@MockBean
	private EmployeeRepository employeeRepository;

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
		given(employeeRepository.findAll((Pageable) any())).willReturn(employeePage);

		// when + then
		mockMvc.perform(get("/"))
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
				.andExpect(redirectedUrl(""));

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
		Employee emp2 = new Employee("Jane", "Smith", "jane.smith@example.com");
		emp2.setId(2L);

		// Создаём список сотрудников
		// Example: Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName").ascending());
		Pageable pageable = PageRequest.of(0, 1);
		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1, emp2), pageable, 2);

		given(employeeRepository.findByFilters(null, null, null, PageRequest.of(0, 10)))
				.willReturn(employeePage);

		// when + then
		mockMvc.perform(get("/showEmployees")
						.param("page", "0")
						.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("showEmployees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 2))
				.andExpect(model().attribute("totalElements", 2L))
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