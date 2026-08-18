package ru.perm.v.hr_admin.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import ru.perm.v.hr_admin.repository.EmployeeRepository;
import ru.perm.v.hr_admin.repository.PositionRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EmployeeControllerRefreshTest {

	@Autowired
	private EmployeeController employeeController;

	@MockBean
	private EmployeeRepository employeeRepository;

	@MockBean
	private PositionRepository positionRepository;

	@Test
	public void refreshEmployeesWithSortingAndPagination() {
		// Given
		Position dev = new Position(1L, "Developer");
		List<Position> positions = List.of(dev);
		when(positionRepository.findAll()).thenReturn(positions);

		Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", dev);
		Employee emp2 = new Employee(2L, "Jane", "Smith", "jane@example.com", dev);
		Page<Employee> page = new PageImpl<>(Arrays.asList(emp1, emp2), PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")), 2);

		// When & Then: вызов listEmployees -> внутренне вызывает refreshEmployees
		when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

		Model model = mock(Model.class);

		employeeController.listEmployees(model, 0, 10, "id", "asc");

		verify(model).addAttribute(eq("employees"), eq(Arrays.asList(emp1, emp2)));
		verify(model).addAttribute(eq("sortField"), eq("id"));
		verify(model).addAttribute(eq("currentPage"), eq(0));
		verify(model).addAttribute(eq("totalPages"), eq(1));
		verify(model).addAttribute(eq("totalElements"), eq(2L));
		verify(model).addAttribute(eq("direction"), eq(Sort.Direction.ASC));
	}

	@Test
	public void shouldApplyDescSortingInRefreshEmployees() {
		// Given
		Position dev = new Position(1L, "Developer");
		when(positionRepository.findAll()).thenReturn(List.of(dev));

		Employee employee1 = new Employee(100L, "Alice", "Brown", "alice@example.com", dev);
		Page<Employee> page = new PageImpl<>(List.of(employee1), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "lastName")), 1);

		when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

		Model model = mock(Model.class);

		// When
		employeeController.listEmployees(model, 0, 10, "lastName", "desc");

		// Then
		verify(model).addAttribute(eq("sortField"), eq("lastName"));
		verify(model).addAttribute(eq("direction"), eq(Sort.Direction.DESC));
		verify(model).addAttribute(eq("employees"), eq(List.of(employee1)));
	}

	@Test
	public void shouldUseDefaultSortIfEmpty() {
		// Given
		Position dev = new Position(1L, "Developer");
		when(positionRepository.findAll()).thenReturn(List.of(dev));

		Employee emp = new Employee("Bob", "Lee", "bob@example.com", dev);
		Page<Employee> page = new PageImpl<>(List.of(emp), PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")), 1);

		when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

		Model model = mock(Model.class);

		// When пустые параметры
		employeeController.listEmployees(model, 0, 10, "", "");

		// должно быть значение по умолчанию
		verify(model).addAttribute(eq("sortField"), eq("id"));
		verify(model).addAttribute(eq("direction"), eq(Sort.Direction.ASC));
	}
}