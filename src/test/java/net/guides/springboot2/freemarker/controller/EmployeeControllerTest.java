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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
		mockMvc.perform(get("/employees/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 1))
				.andExpect(model().attribute("totalElements", 2L))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Firstname1")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Firstname2")));

		verify(employeeRepository, times(1)).findAll(any(Pageable.class));
	}

    @Test
    public void shouldShowCreateForm() throws Exception {
        // Given
        given(positionRepository.findAll()).willReturn(Collections.singletonList(new Position(1L, "Developer")));

        // When & Then
        mockMvc.perform(get("/employees/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_employee"))
                .andExpect(model().attributeExists("employee", "positions"));

		verify(positionRepository, times(1)).findAll();
    }

    @Test
    public void createEmployeeAndRedirect() throws Exception {
        // Given
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", new Position(1L, "Manager"));

		mockMvc.perform(get("/show_employees")); // set previous page (return page)
        // When & Then
        mockMvc.perform(post("/employees/")
                        .flashAttr("employee", employee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/show_employees"));

        verify(employeeRepository, times(1)).save(employee);
    }

	@Test
	public void createEmployeeAndRedirectForEmptyURL() throws Exception {
		Employee employee = new Employee("John", "Doe", "john.doe@example.com", new Position(1L, "Manager"));

		mockMvc.perform(post("/employees")
						.flashAttr("employee", employee))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/employees/"));
	}

    @Test
    public void shouldShowEditFormForExistingEmployee() throws Exception {
        // Given
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
        employee.setId(1L);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(positionRepository.findAll()).willReturn(List.of(position));

        // When & Then
        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_employee"))
                .andExpect(model().attributeExists("employee", "positions"))
                .andExpect(model().attribute("employee", employee));

		verify(positionRepository, times(1)).findAll();
		verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldReturn404WhenEditEmployeeNotFound() {
        // Given
        given(employeeRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
		Exception excp = null;
		try {
			mockMvc.perform(get("/employees/edit/999"));
		} catch (Exception e) {
			excp = e;
		}

		assertNotNull(excp);
		assertEquals("Request processing failed: java.lang.IllegalArgumentException: Employee not exist with id=999", excp.getMessage());
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
                .andExpect(redirectedUrl("/employees/show_employees"));

        verify(employeeRepository).save(updatedEmployee);
    }

    @Test
    public void shouldDeleteEmployeeAndRedirect() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
		employee.setId(1L);

		given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

		Page page = new PageImpl(List.of(employee));
		given(employeeRepository.findAll(any(Pageable.class))).willReturn(page);

		mockMvc.perform((get("/employees/"))); // set current page for return
        mockMvc.perform(delete("/employees/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index/"));

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    public void shouldShowFilteredEmployees() throws Exception {
        // Given
        Employee employee = new Employee(1L,"Firstname1", "Lastname1", "email1@example.com", new Position(1L, "Developer"));
        Page<Employee> page = new PageImpl<>(List.of(employee));

        given(employeeRepository.findByFiltersAndSort(
                eq("Firstname1"), any(), any(), any(Pageable.class)))
                .willReturn(page);

        mockMvc.perform(get("/employees/show_employees")
                        .param("firstName", "Firstname1")
				)
                .andExpect(status().isOk())
                .andExpect(view().name("show_employees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Firstname1")));
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
				eq("f"), eq("l"), eq("e"), eq(pageable))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/employees/show_employees?page=0&size=1&firstName=f&lastName=l&email=e&sortField=id&direction=asc" ))
                .andExpect(status().isOk())
                .andExpect(view().name("show_employees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("totalElements", 2L));
    }

	@Test
	public void showFilterPage() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Mockito.when(this.employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));

		mockMvc.perform(get("/employees/filter" ))
				.andExpect(status().isOk())
				.andExpect(view().name("filter"))
				.andExpect(model().attributeExists("positions"));

		verify(positionRepository, times(1)).findAll();
	}

	@Test
	public void sortByIdAsc() throws Exception {
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
				any(), any(), any(), eq(pageable))).thenReturn(page);

		// When & Then
		// &sortField=id&direction=asc - sort params
		mockMvc.perform(get("/employees/show_employees?page=0&size=1&firstName=f&lastName=l&email=e&sortField=id&direction=asc" ))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("totalElements", 2L));

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq("f"), eq("l"), eq("e"), eq(pageable));
	}

	@Test
	public void sortByFirstnameAsc() throws Exception {

		Position position = new Position(1L, "Developer");

		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(java.util.Arrays.asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		Pageable pageable = PageRequest.of(0, 2, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				eq("firstName1"), eq("lastName1"), eq("email1"), eq(pageable))).thenReturn(page);

		mockMvc.perform(get("/employees/show_employees?page=0&size=2&firstName=firstName1&lastName=lastName1&email=email1&sortField=firstName&direction=asc" ))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("totalElements", 2L));

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq("firstName1"), eq("lastName1"), eq("email1"), any());
	}

	@Test
	public void filterAndSortByDefault() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(java.util.Arrays.asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 1, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				any(), any(), any(), eq(pageable))).thenReturn(page);

		mockMvc.perform(get("/employees/show_employees?page=0&size=1&firstName=f&lastName=l&email=e" ))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("totalElements", 2L));

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq("f"), eq("l"), eq("e"), eq(pageable)); // in pageable sort by ID
	}

	@Test
	public void shouldShowAllEmployeesWithFiltersAndPagination() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position);
		emp1.setId(1L);

		List<Employee> employees = Arrays.asList(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(employeeRepository.findByFiltersAndSort(
				eq("John"), eq(""), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(positionRepository.findAll()).willReturn(Arrays.asList(position));

		// When & Then
		mockMvc.perform(get("/employees/show_employees")
						.param("firstName", "John")
						.param("page", "0")
						.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("currentPage", 0))
				.andExpect(model().attribute("totalPages", 1))
				.andExpect(model().attribute("totalElements", 1L))
				.andExpect(model().attribute("firstName", "John"))
				.andExpect(model().attribute("sortField", "id"))
				.andExpect(model().attribute("sortDirection", "asc"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("John")))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Doe")));
	}

	@Test
	public void shouldApplySortingWhenProvided() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position);
		emp1.setId(1L);

		List<Employee> employees = Arrays.asList(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(positionRepository.findAll()).willReturn(Arrays.asList(position));

		// When & Then
		mockMvc.perform(get("/employees/show_employees")
						.param("sortField", "firstName")
						.param("direction", "desc"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attribute("sortField", "firstName"))
				.andExpect(model().attribute("sortDirection", "desc"));
	}

	@Test
	public void shouldUseDefaultSortByIdAscWhenNoSortParams() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position);
		emp1.setId(1L);

		List<Employee> employees = Arrays.asList(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(positionRepository.findAll()).willReturn(Arrays.asList(position));

		mockMvc.perform(get("/employees/show_employees"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attribute("sortField", "id"))
				.andExpect(model().attribute("sortDirection", "asc"));
	}

	@Test
	public void shouldReturnEmptyListWhenNoMatches() throws Exception {
		// Given
		Page<Employee> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

		given(employeeRepository.findByFiltersAndSort(
				eq("Unknown"), eq(""), eq(""), any(Pageable.class)))
				.willReturn(emptyPage);

		given(positionRepository.findAll()).willReturn(Arrays.asList());

		// When & Then
		mockMvc.perform(get("/employees/show_employees")
						.param("firstName", "Unknown"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("totalElements", 0L))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("Сотрудники не найдены")));
	}

	@Test
	public void sortByDefault() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(java.util.Arrays.asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 10, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(eq(""),eq(""),eq(""), eq(pageable))).thenReturn(page);

		mockMvc.perform(get("/employees/show_employees" ))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("totalElements", 2L));

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq(""), eq(""), eq(""), eq(pageable));
	}

	@Test
	public void listEmployeesWithEmptyParams() throws Exception {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(java.util.Arrays.asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 10, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(eq(""),eq(""),eq(""), eq(pageable))).thenReturn(page);

		mockMvc.perform(get("/employees/show_employees" ))
				.andExpect(status().isOk())
				.andExpect(view().name("show_employees"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("totalElements", 2L));

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq(""), eq(""), eq(""), eq(pageable));
	}
}