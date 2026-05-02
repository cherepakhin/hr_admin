package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(IndexController.class)
public class IndexControllerMvcTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void jumpToViewEmployees() throws Exception {
		Position position = new Position(1L, "Developer");

		Employee emp1 = new Employee("Firstname1", "Lastname1", "emp1@example.com", position);
		emp1.setId(1L);

		Page<Employee> employeePage = new PageImpl<>(Arrays.asList(emp1), PageRequest.of(0, 1), 1);
		Pageable pageable = PageRequest.of(0, 2);
		given(employeeRepository.findAll(any(pageable.getClass()))).willReturn(employeePage);

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("/employees/"));
	}
}
