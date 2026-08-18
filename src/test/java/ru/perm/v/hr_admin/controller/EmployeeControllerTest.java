package ru.perm.v.hr_admin.controller;

import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import ru.perm.v.hr_admin.repository.EmployeeRepository;
import ru.perm.v.hr_admin.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        classes = EmployeeController.class
)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private PositionRepository positionRepository;

    @Test
    public void createEmployeemWhenFirstNameTooLong_DoRedirected() throws Exception {
        Position position1 = new Position(1L, "Position1");
        mockMvc.perform(post("/employees/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "0123456789_0123456789_0123456789_0123456789")  // > 15 символов
                        .param("lastName", "LastName")
                        .param("email", "user@example.com")
                        .param("position.id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_employee"));
    }

    @Test
    public void shouldListEmployeesWithPagination() {
        // Given
        Position position = new Position(1L, "Developer");
        Employee emp1 = new Employee("Firstname1", "Lastname1", "empl1@example.com", position);
        emp1.setId(1L);
        Employee emp2 = new Employee("Firstname2", "Lastname2", "empl2@example.com", position);
        emp2.setId(2L);
        Page<Employee> employeePage = new PageImpl<>(asList(emp1, emp2), PageRequest.of(0, 10), 2);
        when(this.employeeRepository.findAll(any(Pageable.class))).thenReturn(employeePage);

        given(this.employeeRepository.findByFiltersAndSort(
                eq(""), eq(""), any(), eq(""), any(Pageable.class)))
                .willReturn(employeePage);
        given(employeeRepository.findAll(any(PageRequest.class))).willReturn(employeePage);

        try {
            // When & Then
            ResultActions resultActions = mockMvc.perform(get("/employees/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attributeExists("employees"))
                    .andExpect(model().attribute("currentPage", 0))
                    .andExpect(model().attribute("totalPages", 1))
                    .andExpect(model().attribute("totalElements", 2L))
                    .andExpect(model().attribute("employees", asList(emp1, emp2)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        verify(this.employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void shouldShowCreateForm() {
        // Given
        given(this.positionRepository.findAll()).willReturn(Collections.singletonList(new Position(1L, "Developer")));

        // When & Then
        try {
            mockMvc.perform(get("/employees/new"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("create_employee"))
                    .andExpect(model().attributeExists("employee", "positions"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(this.positionRepository, times(1)).findAll();
    }

// подстановка в модель аттрибута:  mockMvc.flashAttr("positions", asList(position1)). Комментарий НЕ УДАЛЯТЬ!

    @Test
    public void redirectToPrevPageForCreateEmployeeFromShowEmployeesPage() {
        // Given
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", new Position(1L, "Manager"));

        try {
            // set previous page (return page)
            mockMvc.perform(get("/show_employees"));
            // When & Then
            mockMvc.perform(post("/employees/")
                            .flashAttr("employee", employee))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(this.employeeRepository, times(1)).save(employee);
    }

    @Test
    public void createEmployeeAndRedirectForEmptyPreviousURL() {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", new Position(1L, "Manager"));

        try {
            mockMvc.perform(post("/employees")
                            .flashAttr("employee", employee))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/employees/"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void showEditFormForEmployee() {
        // Given
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
        employee.setId(1L);

        given(this.positionRepository.findAll()).willReturn(List.of(position));
        given(this.employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // When & Then
        try {
            mockMvc.perform(get("/employees/edit/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("edit_employee"))
                    .andExpect(model().attributeExists("employee", "positions"))
                    .andExpect(model().attribute("employee", employee));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(this.positionRepository, times(1)).findAll();
        verify(this.employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldReturn404WhenEditEmployeeNotFound() {
        // Given
        given(this.employeeRepository.findById(999L)).willReturn(Optional.empty());

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
    public void shouldUpdateEmployeeAndRedirect() {
        // Given
        Employee updatedEmployee = new Employee("Firstname1", "Lastname1", "empl1@example.com", new Position(1L, "Manager"));
        updatedEmployee.setId(1L);

        // When & Then
        try {
            mockMvc.perform(post("/employees/update/1")
                            .flashAttr("employee", updatedEmployee))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(this.employeeRepository).save(updatedEmployee);
    }

    @Test
    public void shouldDeleteEmployeeAndRedirect() {
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
        Long EMPLOYEE_ID = 1L;
        employee.setId(EMPLOYEE_ID);

        given(this.employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        Page<Employee> page = new PageImpl<Employee>(List.of(employee));
        given(this.employeeRepository.existsById(EMPLOYEE_ID)).willReturn(true);
        given(this.employeeRepository.findAll(any(Pageable.class))).willReturn(page);

        try {
            mockMvc.perform((get("/employees/"))); // set current page for return
            mockMvc.perform(get("/employees/delete/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/index/"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(this.employeeRepository).deleteById(1L);
    }

    @Test
    public void deleteEmployeeFromShowEmployeesAndRedirect() {
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", position);
        Long EMPLOYEE_ID = 1L;
        employee.setId(EMPLOYEE_ID);

        doNothing().when(this.employeeRepository).deleteById(1L);

        Page<Employee> page = new PageImpl<Employee>(List.of(employee));
        given(this.employeeRepository.existsById(EMPLOYEE_ID)).willReturn(true);
        given(this.employeeRepository.findAll(any(), any(Sort.class))).willReturn(List.of(employee));
        given(this.employeeRepository.findByFiltersAndSort(any(), any(), any(), any(), any())).willReturn(page);
        given(this.positionRepository.findAll()).willReturn(List.of(position));

        try {
            mockMvc.perform((get("/show_employees"))); // set current page for return
            mockMvc.perform(get("/employees/delete/1"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/index/"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(this.employeeRepository).deleteById(1L);
    }

    @Test
    public void showFilterPage() {
        // Given
        Position position = new Position(1L, "Developer");
        given(this.positionRepository.findAll()).willReturn(List.of(position));

        // When & Then
        try {
            mockMvc.perform(get("/employees/filter_employees"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("filter_employees_form")) // <-- Изменено
                    .andExpect(model().attributeExists("positions"))
                    .andExpect(model().attribute("positions", List.of(position)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        verify(this.positionRepository, times(1)).findAll();
    }

    // сгенерировано gigacode
	@Test
	public void shouldDeleteEmployeeIfExists() {
		// Given
		Long id = 1L;
		given(this.employeeRepository.existsById(id)).willReturn(true);
		doNothing().when(this.employeeRepository).deleteById(id);

		// When & Then
		try {
			ModelAndView result = mockMvc.perform(get("/employees/delete/" + id))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/index/"))
					.andReturn().getModelAndView();

			assertNotNull(result);
			assertEquals("redirect:/index/", result.getViewName());
		} catch (Exception e) {
			fail(e.getMessage());
		}

		verify(this.employeeRepository, times(1)).existsById(id);
		verify(this.employeeRepository, times(1)).deleteById(id);
	}

    // сгенерировано gigacode
	@Test
	public void shouldThrowExceptionWhenDeleteNonExistentEmployee() {
		// Given
		Long id = 999L;
		given(this.employeeRepository.existsById(id)).willReturn(false);

		// When & Then
		Exception exception = null;
		try {
			mockMvc.perform(get("/employees/delete/" + id));
		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Employee not exist with id=" + id));
		verify(this.employeeRepository, never()).deleteById(any());
	}

    @Test
    public void refreshEmployeesWithSortingAndPagination() {
        // Given
        Position position = new Position(1L, "Developer");
        Employee emp1 = new Employee("Firstname1", "Lastname1", "empl1@example.com", position);
        emp1.setId(1L);
        Employee emp2 = new Employee("Firstname2", "Lastname2", "empl2@example.com", position);
        emp2.setId(2L);
        Page<Employee> employeePage = new PageImpl<>(asList(emp1, emp2), PageRequest.of(0, 10), 2);
        when(this.employeeRepository.findAll(any(Pageable.class))).thenReturn(employeePage);

        given(this.employeeRepository.findByFiltersAndSort(
                eq(""), eq(""), any(), eq(""), any(Pageable.class)))
                .willReturn(employeePage);
        given(employeeRepository.findAll(any(PageRequest.class))).willReturn(employeePage);

        try {
            // When & Then
            ResultActions resultActions = mockMvc.perform(get("/employees/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attributeExists("employees"))
                    .andExpect(model().attribute("currentPage", 0))
                    .andExpect(model().attribute("totalPages", 1))
                    .andExpect(model().attribute("totalElements", 2L))
                    .andExpect(model().attribute("employees", asList(emp1, emp2)))
                    .andDo(print());
        } catch (Exception e) {
            fail(e.getMessage()) ;
        }
        verify(this.employeeRepository, times(1)).findAll(any(Pageable.class));
    }
    /*

     */
	/*


	@Test
	public void sortByIdAsc() {
		// Given
		Position position = new Position(1L, "Developer");
		// Given
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 1, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				any(), any(), any(), any(), eq(pageable))).thenReturn(page);

		// When & Then
		// &sortField=id&direction=asc - sort params
		try {
			mockMvc.perform(get("/employees/show_employees?page=0&size=1&firstName=f&lastName=l&email=e&sortField=id&direction=asc"))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 2L));

			verify(this.employeeRepository, times(1)).findByFiltersAndSort(
					eq("f"), eq("l"), any(), eq("e"), eq(pageable));
		} catch (Exception exptn) {
			fail(exptn.getMessage()) ;
		}
	}

	@Test
	public void sortByFirstnameAsc() {

		Position position = new Position(1L, "Developer");

		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		Pageable pageable = PageRequest.of(0, 2, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				eq("firstName1"), eq("lastName1"), any(),  eq("email1"), eq(pageable))).thenReturn(page);

		try {
			mockMvc.perform(get("/employees/show_employees?page=0&size=2&firstName=firstName1&lastName=lastName1&email=email1&sortField=firstName&direction=asc"))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 2L));
		} catch (Exception e) {
			fail(e.getMessage());
		}

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq("firstName1"), eq("lastName1"), any(), eq("email1"), any());
	}

	@Test
	public void filterAndSortByDefault() {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "lastName");
		Pageable pageable = PageRequest.of(0, 1, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				any(), any(), any(), any(), eq(pageable))).thenReturn(page);

		try {
			mockMvc.perform(get("/employees/show_employees?page=0&size=1&firstName=f&lastName=l&email=e" ))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 2L));
		} catch (Exception e) {
			fail(e.getMessage());
		}

		// verify params !!! see mockMvc.perform(get("/employees/show_employees?page=0&size=1&firstName=f&lastName=l&email=e" ))
		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq("f"), eq("l"), any(), eq("e"), eq(pageable));
	}

	@Test
	public void shouldShowAllEmployeesWithFiltersAndPagination() {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position);
		emp1.setId(1L);

		List<Employee> employees = List.of(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(this.employeeRepository.findByFiltersAndSort(
				eq("John"), eq(""), any(), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(this.positionRepository.findAll()).willReturn(asList(position));

		try {
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
					.andExpect(model().attribute("sortField", "lastName"))
					.andExpect(model().attribute("direction", "asc"))
					.andExpect(content().string(org.hamcrest.Matchers.containsString("John")))
					.andExpect(content().string(org.hamcrest.Matchers.containsString("Doe")));
		} catch(Exception excptn){
			fail(excptn.getMessage());
		}
	}

	@Test
	public void shouldApplySortingWhenProvided() {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position);
		emp1.setId(1L);

		List<Employee> employees = asList(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(this.employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), any(), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(this.positionRepository.findAll()).willReturn(asList(position));

		try {
			// When & Then
			mockMvc.perform(get("/employees/show_employees")
							.param("sortField", "firstName")
							.param("direction", "desc"))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attribute("sortField", "firstName"))
					.andExpect(model().attribute("direction", "desc"));
		} catch(Exception exception ){
			fail(exception.getMessage());
		}
	}

	@Test
	public void shouldUseDefaultSortByLastNameAscWhenNoSortParams() {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position);
		emp1.setId(1L);

		List<Employee> employees = asList(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(this.employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), any(), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(this.positionRepository.findAll()).willReturn(asList(position));

		try {
			mockMvc.perform(get("/employees/show_employees"))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attribute("sortField", "lastName"))
					.andExpect(model().attribute("direction", "asc"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void shouldReturnEmptyListWhenNoMatches() {
		// Given
		Page<Employee> emptyPage = new PageImpl<>(asList(), PageRequest.of(0, 10), 0);

		given(employeeRepository.findByFiltersAndSort(
				eq("Unknown"), eq(""), any(), eq(""), any(Pageable.class)))
				.willReturn(emptyPage);

		given(this.positionRepository.findAll()).willReturn(asList());

		// When & Then
		try {
			mockMvc.perform(get("/employees/show_employees")
							.param("firstName", "Unknown"))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 0L))
					.andExpect(content().string(org.hamcrest.Matchers.containsString("Сотрудники не найдены")));
		} catch (Exception e) {
			fail(e.getMessage()) ;
		}
	}

	@Test
	public void sortByDefault() {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "id");

		List<Position> positions = Arrays.asList(position);
		Mockito.when(this.positionRepository.findAll()).thenReturn(positions);
		Mockito.when(this.employeeRepository.findByFiltersAndSort(eq(""),eq(""),any(), eq(""), any(Pageable.class))).thenReturn(page);

		try {
			mockMvc.perform(get("/employees/show_employees" ))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 2L));
		} catch (Exception e) {
			fail(e.getMessage()) ;
		}

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq(""), eq(""), any(), eq(""), any());
	}

	@Test
	public void listEmployeesWithEmptyParams() {
		Position position = new Position(1L, "Developer");
		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(asList(emp1, emp2));

		List<Position> positions = Arrays.asList(position);
		Mockito.when(this.positionRepository.findAll()).thenReturn(positions);

		// так работает (any(Pageable.class))
		// firstName. lastName, positionIds, email, pagination
		// Mockito.when(this.employeeRepository.findByFiltersAndSort(
		//		eq(""), eq(""), anyList(), eq(""), any(Pageable.class))).thenReturn(page);

		Sort sort = Sort.by(Sort.Direction.ASC, "lastName");
		Pageable pageable = PageRequest.of(0, 10, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), any(List.class), eq(""), eq(pageable))).thenReturn(page);
		try {
			mockMvc.perform(get("/employees/show_employees" ))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 2L));
		} catch (Exception e) {
			fail(e.getMessage()) ;
		}

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq(""), eq(""), any(), eq(""), eq(pageable));
	}

	@Test
	public void sortByPosition() {

		Position position1 = new Position(1L, "Position1");
		Position position2 = new Position(2L, "Position2");

		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position1);
		emp1.setId(1L);
		Employee emp2 = new Employee("Firstname2", "Lastname2", "emp2@example.com", position2);
		emp2.setId(2L);
		Page<Employee> page = new PageImpl<>(asList(emp1, emp2));

		Sort sort = Sort.by(Sort.Direction.ASC, "position.name");
		Pageable pageable = PageRequest.of(0, 10, sort);

		Mockito.when(this.employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), anyList(), eq(""), eq(pageable))).thenReturn(page);

		try {
			mockMvc.perform(get("/employees/show_employees?sortField=position&direction=asc"))
					.andExpect(status().isOk())
					.andExpect(view().name("show_employees"))
					.andExpect(model().attributeExists("employees"))
					.andExpect(model().attribute("totalElements", 2L));
		} catch (Exception e) {
			fail(e.getMessage());
		}

		verify(this.employeeRepository, times(1)).findByFiltersAndSort(
				eq(""), eq(""), anyList(), eq(""), eq(pageable));
	}

	public void showAllEmployeesForEmptyPositionId() {

	}


	@Test
	public void shouldReturnCreateFormWithErrorWhenFirstNameTooShort() throws Exception {
		Position position1 = new Position(1L, "Position1");
		Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", position1);
		emp1.setId(1L);

		List<Employee> employees = asList(emp1);
		Page<Employee> employeePage = new PageImpl<>(employees, PageRequest.of(0, 10), 1);

		given(this.employeeRepository.findByFiltersAndSort(
				eq(""), eq(""), any(), eq(""), any(Pageable.class)))
				.willReturn(employeePage);

		given(this.positionRepository.findAll()).willReturn(asList(position1));

		mockMvc.perform(post("/employees/")
					.flashAttr("positions", asList(position1)) // подстановка в модель аттрибута !!!
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("firstName", "Jo")  // слишком короткое (менее 3 символов)
					.param("lastName", "Doe")
					.param("email", "jo@example.com")
					.param("position.id", "1"))
				.andExpect(model().attributeExists("firstName"))
				.andExpect(status().isOk())
				.andExpect(view().name(NamesView.CREATE_EMPLOYEE))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("firstName", "Jo"));

		verify(employeeRepository, never()).save(any(Employee.class));
	}

	@Test
	public void shouldSaveEmployeeWhenValidData() throws Exception {
		// Given
		Position position = new Position(1L, "Developer");
		when(positionRepository.findAll()).thenReturn(List.of(position));
		when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

		// When & Then
		mockMvc.perform(post("/employees/")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("firstName", "John")
						.param("lastName", "Doe")
						.param("email", "john@example.com")
						.param("position.id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		verify(employeeRepository).save(argThat(emp ->
				"John".equals(emp.getFirstName()) &&
						"Doe".equals(emp.getLastName()) &&
						"john@example.com".equals(emp.getEmail()) &&
						emp.getPosition().getId().equals(1L)
		));
	}

	@Test
	public void shouldAddErrorToModelWhenBindingFails() {
		// Given
		Model model = mock(Model.class);
		BindingResult bindingResult = mock(BindingResult.class);

		when(bindingResult.hasErrors()).thenReturn(true);
		when(bindingResult.getAllErrors()).thenReturn(List.of(
				new org.springframework.validation.FieldError("employee", "firstName",
						"First name must be between 3 to 15 characters long.")
		));

		Employee invalidEmployee = new Employee();
		invalidEmployee.setFirstName("A");

		EmployeeController employeeController = new EmployeeController();
		employeeController.setEmployeeRepository(employeeRepository);
		// When
		String viewName = employeeController.createEmployee(invalidEmployee, bindingResult, model);

		// Then
		verify(model).addAttribute(eq("firstName"), eq("A"));
		verify(model).addAttribute(eq("error"), eq("First name must be between 3 to 15 characters long.\n"));
		verify(employeeRepository, never()).save(any(Employee.class));
		assert viewName.equals(NamesView.CREATE_EMPLOYEE);
	}

 */
}
