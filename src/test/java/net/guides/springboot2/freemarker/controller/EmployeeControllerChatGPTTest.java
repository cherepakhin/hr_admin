package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Тест сгенерирован https://chatgpt.org/chat
 * После генерации исправлены мелкие помарки
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerChatGPTTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeRepository employeeRepository;

	@MockBean
	private PositionRepository positionRepository;

	private Employee sample;
	

	@BeforeEach
	void setup() {
		sample = new Employee();
		sample.setId(1L);
		sample.setFirstName("Иван");
		sample.setLastName("Иванов");
		sample.setEmail("ivan@example.com");
		// заполните остальные поля по необходимости
	}

	@Test
	void testListEmployees() throws Exception {
		// Prepare a page of employees
	
		Employee e1 = sample;
		Page<Employee> page = new PageImpl<>(Arrays.asList(e1), PageRequest.of(0, 10, Sort.by("id")), 1);
		given(this.employeeRepository.findAll(any(Pageable.class))).willReturn(page);

		mockMvc.perform(MockMvcRequestBuilders.get("/employees/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("employees"))
				.andExpect(MockMvcResultMatchers.model().attribute("totalPages", 1))
				.andExpect(MockMvcResultMatchers.view().name("index")); // NamesView.INDEX должен соответствовать "index"
	}

	@Test
	void testShowCreateForm() throws Exception {
		given(positionRepository.findAll()).willReturn(Collections.emptyList());

		mockMvc.perform(MockMvcRequestBuilders.get("/employees/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("employee"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("positions"))
				.andExpect(MockMvcResultMatchers.view().name("create_employee")); // NamesView.CREATE_EMPLOYEE
	}

	@Test
	void testCreateEmployee() throws Exception {
		// здесь можно просто проверить редирект после сохранения
		mockMvc.perform(MockMvcRequestBuilders.post("/employees/")
						.param("firstName", "Илья")
						.param("lastName", "Петров")
						.param("email", "ilya@example.com"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
		verify(employeeRepository).save(ArgumentMatchers.any(Employee.class));
	}

	@Test
	void testShowEditFormExists() throws Exception {
		given(employeeRepository.findById(1L)).willReturn(Optional.of(sample));
		given(positionRepository.findAll()).willReturn(Collections.emptyList());

		mockMvc.perform(MockMvcRequestBuilders.get("/employees/edit/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("employee"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("positions"))
				.andExpect(MockMvcResultMatchers.view().name("edit_employee")); // NamesView.EDIT_EMPLOYEE
	}

	@Test
	void testShowEditFormNotExists() throws Exception {
		given(employeeRepository.findById(999L)).willReturn(Optional.empty());

		try {
			mockMvc.perform(MockMvcRequestBuilders.get("/employees/edit/999")).andReturn();
		} catch (Exception e) {
			assertEquals("Request processing failed: java.lang.IllegalArgumentException: Employee not exist with id=999", e.getMessage());
		}


	}

	@Test
	void testUpdateEmployee() throws Exception {
		// Предположим, что employee с id=1 существует
		mockMvc.perform(MockMvcRequestBuilders.post("/employees/update/1")
						.param("firstName", "Игорь")
						.param("lastName", "Сидоров")
						.param("email", "igor@example.com"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		verify(employeeRepository).save(ArgumentMatchers.any(Employee.class));
	}

	@Test
	void testShowAllEmployeesWithFilters() throws Exception {
		Employee emp = sample;
		Page<Employee> page = new PageImpl<>(Collections.singletonList(emp), PageRequest.of(0, 10), 1);
		given(employeeRepository.findByFiltersAndSort("Ив", "", List.of(), "", PageRequest.of(0, 10, Sort.by("id"))))
				.willReturn(page);

		mockMvc.perform(MockMvcRequestBuilders.get("/employees/show_employees")
						.param("firstName", "Ив")
						.param("lastName", "")
						.param("email", "")
						.param("sortField", "id")
						.param("direction", "asc"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("employees"))
				.andExpect(MockMvcResultMatchers.view().name("show_employees")); // NamesView.SHOW_EMPLOYEES
	}
}