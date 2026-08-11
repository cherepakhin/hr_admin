package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class PositionRestControllerTest {

	@Test
	void canNotDeletePosition() {
		PositionRepository mockPositionRepo = mock(PositionRepository.class);
		EmployeeRepository mockEemployeeRepository = mock(EmployeeRepository.class);
		Employee employee1 = new Employee();
		Long EMPLOYEE_ID = 10L;
		employee1.setId(EMPLOYEE_ID);

		Position position = new Position();
		Long POSITION_ID = 20L;
		position.setId(POSITION_ID);
		List<Employee> employees = List.of(employee1);

		Mockito.when(mockEemployeeRepository.findAllByPosition(POSITION_ID)).thenReturn(employees);

		PositionRestController positionRestController = new PositionRestController(mockPositionRepo,mockEemployeeRepository);
		String answerCanDelete = positionRestController.canDeletePosition(POSITION_ID, any());

		assertEquals("{\"deleteable\": false}", answerCanDelete);

	}

	@Test
	void canDeletePosition() {
		PositionRepository mockPositionRepo = mock(PositionRepository.class);
		EmployeeRepository mockEemployeeRepository = mock(EmployeeRepository.class);
		Position position = new Position();
		Long POSITION_ID = 20L;
		position.setId(POSITION_ID);

		Mockito.when(mockEemployeeRepository.findAllByPosition(POSITION_ID)).thenReturn(List.of());

		PositionRestController positionRestController = new PositionRestController(mockPositionRepo,mockEemployeeRepository);
		String answerCanDelete = positionRestController.canDeletePosition(POSITION_ID, any());

		assertEquals("{\"deleteable\": true}", answerCanDelete);
	}
}