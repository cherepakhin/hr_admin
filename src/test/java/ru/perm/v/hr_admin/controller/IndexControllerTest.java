package ru.perm.v.hr_admin.controller;

import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import ru.perm.v.hr_admin.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(IndexController.class)
public class IndexControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void jumpToViewEmployees() throws Exception {
		Position position = new Position(1L, "Developer");

		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);

		Page<Employee> employeePage = new PageImpl<>(List.of(emp1), PageRequest.of(0, 1), 1);
		Pageable pageable = PageRequest.of(0, 2);
		given(this.employeeRepository.findAll(any(pageable.getClass()))).willReturn(employeePage);

		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("/employees/"));
	}
}
